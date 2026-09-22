# Bank Loan System - Backend API

API REST transaccional para la gestión y aprobación de préstamos bancarios. Desarrollada bajo principios de diseño robustos, seguridad sin estado y optimización de lectura.

## 🛠️ Stack Tecnológico
- **Lenguaje:** Java 17
- **Framework:** Spring Boot 4.0.8 (Spring MVC)
- **Persistencia:** Spring Data JPA + Hibernate + Conector MySQL
- **Seguridad:** Spring Security + OAuth2 Resource Server (JWT)
- **Caché:** Spring Cache (ConcurrentMap)
- **Pruebas:** JUnit 5 + Mockito

## 🏗️ Decisiones de Arquitectura & Patrones (Sustentación Senior)
- **Arquitectura Hexagonal Simplificada:** El núcleo de la aplicación se divide en `domain` (modelos de negocio puros y excepciones de dominio), `application` (casos de uso/servicios y DTOs de contrato) e `infrastructure` (controladores web, seguridad, mappers y persistencia). Esto aísla las reglas de negocio de los frameworks externos y bases de datos.
- **Seguridad Moderna Standalone:** Se delegó el análisis de cabeceras manuales al motor nativo de `oauth2ResourceServer`. Spring Security valida de forma asíncrona la firma criptográfica de los Bearer Tokens sin estado.
- **Transaccionalidad & Consistencia:** Las operaciones críticas de mutación de estado (como `updateLoanStatus` y `createLoan`) implementan la anotación `@Transactional` para garantizar la atomicidad en la base de datos MySQL ante fallos concurrentes.
- **Estrategia de Caché e Invalidación:** Se implementó `@Cacheable` para reducir el impacto de consultas repetitivas en las lecturas de listas de préstamos. Para evitar el problema de lecturas sucias (dirty reads), se acopló `@CacheEvict(allEntries = true)` en los métodos de mutación, forzando la sincronización inmediata del caché en memoria tras una aprobación o creación.
- **Mapeo Explícito vs CGLIB:** Se evitó el uso de librerías de mapeo opacas (como MapStruct o ModelMapper) implementando un componente manual `LoanMapper`. Esto elimina sobrecostos de reflexión en tiempo de ejecución y otorga total control de tipos.

## 🧪 Cobertura de Pruebas Unitarias
Se diseñó una suite exhaustiva con JUnit 5 y Mockito enfocada en las ramificaciones lógicas de `LoanService`, cubriendo flujos exitosos, control de estados inválidos (intentar modificar préstamos no pendientes) y excepciones de negocio personalizadas.
