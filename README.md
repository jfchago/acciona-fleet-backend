# Backend

## Local SQL Server Express

The default `local` profile runs Flyway against Microsoft SQL Server Express. No credentials are stored in the repository.
Docker Desktop (or another Docker-compatible daemon) and a password meeting SQL Server's complexity requirements are prerequisites.

```powershell
$env:MSSQL_SA_PASSWORD = "choose-a-local-password"
docker compose up -d sqlserver sqlserver-init
$env:DB_PASSWORD = $env:MSSQL_SA_PASSWORD
mvn spring-boot:run
```

The Compose init service creates the `${DB_NAME:-backend}` database. For an existing SQL Server instance, set `DB_URL`, `DB_USERNAME`, `DB_PASSWORD`, and optionally `DB_NAME` instead. Use `DB_ENCRYPT=true` with a trusted certificate in environments where encryption is required.

Then check `http://localhost:8080/api/v1/service/status` or create an example:

```powershell
$body = '{"value":"local sample"}'
Invoke-RestMethod -Method Post -Uri http://localhost:8080/api/v1/examples -ContentType 'application/json' -Body $body
```

## Tests

`mvn test` uses an in-memory H2 database and does not require SQL Server or Docker. H2 is test-only; production migrations target SQL Server and use SQL Server-compatible `uniqueidentifier` and `datetimeoffset` types.
# Backend

## Database profiles

The default `local` profile connects to the SQL Server instance installed on
Windows:

- Server: `localhost:1433`
- Database: `AccionaFleet`
- Authentication: Windows integrated authentication for the logged-in OS user

The Microsoft JDBC native authentication library must be available on the
Windows `PATH`:
`mssql-jdbc_auth-<driver-version>-x64.dll`.

Run locally with:

```powershell
mvn spring-boot:run
```

For a disposable SQL Server Express instance, start the existing
`docker-compose.yml` and run with the `docker` profile:

```powershell
$env:MSSQL_SA_PASSWORD = "A_strong_password_123!"
docker compose up -d
mvn spring-boot:run -Dspring-boot.run.profiles=docker
```

The Docker SQL Server is exposed on `localhost:1434` so it does not conflict
with the Windows SQL Server on port `1433`.
