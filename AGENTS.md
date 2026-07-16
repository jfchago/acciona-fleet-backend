# AGENTS.md — Backend

## Propósito

Backend de AccionaFleet. Este archivo es el contrato técnico para MetaContext, MetaCoder y MetaReviewer. Las reglas son aplicables a cualquier cambio dentro de este repositorio y complementan las instrucciones del workspace raíz.

## Stack tecnológico

- Java 17; existe un perfil Maven `java25` para evolución controlada, pero Java 17 es la línea base.
- Spring Boot 4.1.0 y Maven.
- Spring MVC, Validation, Actuator, Data JPA, Security y OAuth2 Resource Server.
- Flyway con migraciones SQL para Microsoft SQL Server; H2 se usa únicamente en tests.
- ArchUnit para reglas arquitectónicas y Spring Boot Test/Security Test para pruebas.
- Docker Compose para SQL Server local.
- API HTTP REST versionada bajo `/api/v1`; el contrato OpenAPI compartido vive en `../../.metacontext/deliverables/contracts/OpenApi.yaml` cuando esté disponible.

## Arquitectura y estructura

Aplicar arquitectura hexagonal/modular:

- `shared/domain`: modelos y conceptos de dominio sin dependencias de Spring.
- `shared/application/port/in`: casos de uso expuestos por la aplicación.
- `shared/application/port/out`: puertos de persistencia o integraciones.
- `shared/application/service`: implementación de casos de uso.
- `shared/adapters/in/rest`: controladores, requests y responses HTTP.
- `shared/adapters/out`: persistencia e integraciones externas.
- `configuration`: composición de dependencias y configuración transversal.
- `src/main/resources/db/migration`: migraciones Flyway versionadas.

Las dependencias deben apuntar hacia el dominio y la aplicación. Los controladores no contienen lógica de negocio ni acceden directamente a repositorios; usan puertos de entrada. La composición concreta se realiza en `configuration`.

## Reglas de implementación

1. Antes de codificar, inspeccionar el grafo de conocimiento con `search_graph`, `trace_path` y `get_code_snippet`; buscar patrones existentes antes de crear clases nuevas.
2. Mantener nombres explícitos y consistentes con el código existente: `*Controller`, `*UseCase`, `*Service`, `*Response`, `*Request` y `*Repository`.
3. Separar DTOs HTTP de objetos de dominio. Validar entradas con Bean Validation y devolver errores mediante Problem Details; no exponer stack traces ni detalles sensibles.
4. Versionar endpoints y documentar cambios de contrato en OpenAPI. Si cambia el contrato, actualizar el artefacto compartido y avisar del impacto en frontend.
- **Database First:** antes de crear tablas nuevas o migraciones Flyway, localizar y mapear la tabla, vista y relaciones equivalentes de la base de datos SQL Server del legacy. Se debe reutilizar el modelo legacy siempre que cubra la funcionalidad requerida.
- Priorizar la persistencia mediante JPA, Hibernate y Spring Data JPA. Utilizar Spring JDBC Template únicamente cuando sea estrictamente necesario y exista una limitación justificada de JPA/Hibernate.
5. No crear ni modificar esquema mediante `ddl-auto`; toda evolución de base de datos debe ser una migración Flyway incremental, reversible operacionalmente y compatible con SQL Server.
6. Usar `UUID`/`uniqueidentifier` y `OffsetDateTime`/`datetimeoffset` cuando correspondan a las convenciones actuales. Mantener `open-in-view=false`.
7. Configuración, URLs, credenciales, issuer y secretos siempre por variables de entorno o perfiles; nunca hardcodeados ni registrados en logs.
8. Respetar los perfiles `local` y `secure`. Las rutas nuevas deben definir explícitamente su requisito de autenticación/autorización; no asumir que el perfil local representa producción.
9. Mantener Actuator limitado a endpoints operativos aprobados y preservar health/readiness, apagado graceful y trazabilidad configurada.
10. Preferir componentes Spring y dependencias ya presentes. No añadir librerías sin justificar necesidad, licencia, soporte, impacto de seguridad y alternativa existente.

## Pruebas y calidad

- Ejecutar `mvn test` para cada cambio funcional; no requerir SQL Server para la suite unitaria.
- Añadir pruebas de caso de uso con patrón Arrange/Act/Assert y pruebas de integración cuando cambien persistencia, seguridad o HTTP.
- Mantener y ampliar `ArchitectureTest` cuando se introduzcan módulos o reglas nuevas.
- Usar Testcontainers solo para pruebas que necesiten comportamiento real de infraestructura.
- No desactivar tests, ArchUnit, validaciones o análisis para hacer pasar el build.
- Antes de entregar, ejecutar `mvn verify` cuando el cambio afecte integración, migraciones o configuración.

## Seguridad para MetaReviewer

Revisar siempre autenticación/autorización, validación y límites de entrada, inyección SQL, exposición de errores, secretos, CORS, configuración de Actuator, logs con datos personales, dependencias vulnerables y compatibilidad de migraciones. Clasificar hallazgos como `CRITICAL`, `HIGH`, `MEDIUM` o `LOW`, incluyendo archivo/línea, impacto, evidencia y corrección propuesta.

## Flujo MetaContext / MetaCoder / MetaReviewer

MetaCoder debe: (1) leer este contrato y el plan técnico, (2) consultar el grafo antes de modificar, (3) implementar por capas respetando los puertos, (4) actualizar contrato/migraciones si aplica, y (5) ejecutar las pruebas indicadas.

MetaReviewer debe verificar: arquitectura y dependencias, contrato REST/OpenAPI, seguridad, migraciones, observabilidad, cobertura de comportamiento y calidad de tests. Debe separar defectos introducidos por el cambio de deuda preexistente y no aprobar con tests desactivados.

Toda implementación debe entregar un resumen de cambios, decisiones, pruebas ejecutadas, riesgos y tareas pendientes. No se deben realizar commits, merges ni pushes desde estos agentes salvo instrucción explícita.

## Comandos de referencia

```powershell
mvn test
mvn verify
mvn spring-boot:run
```
