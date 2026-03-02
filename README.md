# Patient Management - Docker Compose Notes

This project uses Docker Compose to run both containers together:
- `patient-service` (Spring Boot app)
- `patient-service-db` (PostgreSQL 17)

## 1) Prepare local DB data folder

Run from `patient-management`:

```powershell
mkdir .docker\postgres-data -Force
```

## 2) Build and start all services

```powershell
docker compose up -d --build
```

## 3) Useful checks

```powershell
docker compose ps
docker compose logs -f patient-service-db
docker compose logs -f patient-service
docker compose config
```

## 4) Stop services

```powershell
docker compose down
```

If you also want to remove DB data volume mapping:

```powershell
docker compose down -v
```

## 5) Access

- App base URL: `http://localhost:4000`
- Swagger UI: `http://localhost:4000/swagger-ui.html`

## Docker Compose attributes explained

### Top level

- `services`: Defines each containerized component in this stack.
- `networks`: Defines virtual networks used by services.

### `patient-service-db` service

- `image: postgres:17`: Uses a stable major version and avoids breaking changes from `latest`.
- `container_name`: Fixed container name (`patient-service-db`).
- `restart: unless-stopped`: Restarts automatically unless you manually stop it.
- `environment`: Sets PostgreSQL init values:
  - `POSTGRES_DB`: database name (`patientdb`)
  - `POSTGRES_USER`: DB username (`patient_user`)
  - `POSTGRES_PASSWORD`: DB password (`patient_pass`)
- `ports: "5432:5432"`: Maps host port `5432` to container port `5432`.
- `volumes: ./.docker/postgres-data:/var/lib/postgresql/data`: Persists DB data on your machine.
- `healthcheck`: Verifies DB readiness before dependent services start.
  - `test`: command used for readiness check (`pg_isready`)
  - `interval`: check frequency
  - `timeout`: max wait per check
  - `retries`: failures before unhealthy
  - `start_period`: grace period before counting failures
- `networks`: Attaches DB container to the Compose network for this project (`internal`).

### `patient-service` service

- `build.context: ./patient-service`: Builds image from `patient-service/Dockerfile`.
- `image: patient-service:latest`: Tags built image name.
- `container_name`: Fixed container name (`patient-service`).
- `restart: unless-stopped`: Restarts automatically unless manually stopped.
- `depends_on` with `condition: service_healthy`: Starts app only after DB healthcheck passes.
- `ports: "4000:4000"`: Exposes app on host `localhost:4000`.
- `environment`: Overrides Spring Boot config at runtime:
  - `SERVER_PORT=4000`
  - PostgreSQL datasource URL, user, password, and driver
  - Hibernate dialect and DDL mode (`update`)
  - SQL init mode (`always`)
- `networks`: Attaches app container to the same Compose network (`internal`).

### Network

- `internal`: Shared network for service-to-service communication.
- Compose creates and manages the real Docker network name automatically (project-scoped), which avoids collisions with old manually created networks.

## Notes

- `application.properties` currently defaults to `server.port=4001`, but Compose sets `SERVER_PORT=4000`.
- `application.properties` has H2 settings commented out; Compose environment switches runtime datasource to PostgreSQL.

## Troubleshooting

If DB startup still fails because of old local PostgreSQL files, remove only local dev data and recreate:

```powershell
docker compose down
Remove-Item -Recurse -Force .docker\postgres-data
mkdir .docker\postgres-data -Force
docker compose up -d --build
```
