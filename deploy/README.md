# Деплой в Kubernetes (k3s)

Манифесты в `deploy/k8s/` (Kustomize). Рассчитано на одну ноду k3s на VPS,
но подойдёт любому кластеру.

## Состав

| Ресурс | Что |
|---|---|
| `namespace.yaml` | namespace `evfunnytest` |
| `postgres.yaml` | StatefulSet + headless Service, PVC `2Gi` (StorageClass по умолчанию — в k3s это `local-path`) |
| `redis.yaml` | Deployment + Service, без персистентности (`--save "" --appendonly no`) |
| `bot.yaml` | Deployment бота, `replicas: 1`, `strategy: Recreate` (long polling не терпит два инстанса), initContainer ждёт БД и Redis |
| `secret.example.yaml` | шаблон секрета, **не** входит в `kustomization.yaml` |

Бот ходит в `postgres:5432` и `redis:6379` по именам сервисов (`EV_POSTGRES_HOST` / `EV_REDIS_HOST` заданы в `bot.yaml`).

## Разовый bootstrap кластера

```bash
# 1. k3s
curl -sfL https://get.k3s.io | sh -
# kubeconfig: /etc/rancher/k3s/k3s.yaml

# 2. namespace + секрет (значения — свои)
kubectl create namespace evfunnytest
kubectl -n evfunnytest create secret generic evfunnytest-secrets \
  --from-literal=EV_BOT_NAME=my_bot \
  --from-literal=EV_BOT_TOKEN=123456:ABC... \
  --from-literal=EV_POSTGRES_DB=evfunnytest \
  --from-literal=EV_POSTGRES_USERNAME=evfunnytest \
  --from-literal=EV_POSTGRES_PASSWORD=$(openssl rand -hex 16)
```

## Доступ к образу в GHCR

Если пакет `ghcr.io/evmetatron/evfunnytestbot` **public** — ничего не нужно.

Если **private** — завести pull-секрет и сослаться на него:

```bash
kubectl -n evfunnytest create secret docker-registry ghcr-pull \
  --docker-server=ghcr.io \
  --docker-username=<github-user> \
  --docker-password=<PAT с read:packages>
kubectl -n evfunnytest patch serviceaccount default \
  -p '{"imagePullSecrets":[{"name":"ghcr-pull"}]}'
```

## Ручной деплой

```bash
kubectl apply -k deploy/k8s

# конкретный образ вместо :latest
cd deploy/k8s
kustomize edit set image ghcr.io/evmetatron/evfunnytestbot=ghcr.io/evmetatron/evfunnytestbot:sha-<commit>
kubectl apply -k .
```

## Автоматический деплой

`.github/workflows/main.yml`: `analyse` → `build-image` (пуш в GHCR) → `deploy`.

Джоба `deploy` включается только на пуш тега `vX.Y.Z` (`github.ref_type == 'tag'`) —
то есть на релиз (см. корневой `README.md`, раздел «Релиз»), не на каждый merge в
master. Она проставляет в `kustomization.yaml` тег образа, соответствующий тегу
релиза, и делает `kubectl apply -k`.

Требует секрет **`KUBE_CONFIG`** — base64 от kubeconfig с доступом к namespace
`evfunnytest` (репозиторный секрет или секрет GitHub Environment `production`,
на который смотрит джоба). Пока секрета нет, джоба сама себя пропускает
(предупреждение в логе), остальной пайплайн не ломается — образ всё равно
собирается и публикуется.

## Известные ограничения

- У бота нет HTTP-эндпоинта, поэтому нет liveness/readiness-проб. Зависший процесс
  сам не перезапустится. Чинится добавлением `spring-boot-starter-actuator` и пробы
  на `/actuator/health` — отдельная задача.
- Redis без персистентности: рестарт Redis обрывает тесты, которые проходят в этот
  момент (у `@RedisHash`-сущностей есть TTL, данные всё равно временные).
