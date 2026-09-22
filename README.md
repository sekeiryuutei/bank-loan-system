# Bank Loan Management System

Este repositorio contiene una solución Full Stack de nivel producción para un **Sistema de Gestión de Préstamos Bancarios**. El proyecto ha sido diseñado bajo los principios de Arquitectura Hexagonal/Limpia, Seguridad sin estado, Reactividad fina en el cliente (Zoneless) y empaquetamiento e infraestructura automatizada con Docker.

## 🏗️ Estructura del Repositorio
El proyecto se encuentra completamente desacoplado y estructurado de la siguiente manera:
```text
bank-loan-system/
├── backend/          # API REST transaccional en Java + Spring Boot
│   └── README.md     # Documentación técnica y cobertura del Backend
├── frontend/         # Cliente SPA moderno en Angular v22 (Zoneless + Signals)
│   └── README.md     # Decisiones de diseño y patrones de reactividad del Frontend
└── docker-compose.yml# Orquestación de infraestructura local de contenedores
```

## 🛠️ Stack Tecnológico Global

### Backend (Core de Negocio):
- **Java 17** + **Spring Boot 4.0.8** (Spring MVC).
- **Spring Data JPA** + **Hibernate** + **MySQL 8.0**.
- **Spring Security** + **OAuth2 Resource Server** para la validación asíncrona de firmas criptográficas de **JWT Bearer Tokens**.
- **Spring Cache** (`ConcurrentMap`) para optimización y mitigación de lecturas repetitivas.
- **JUnit 5** + **Mockito** para la suite de pruebas unitarias robustas.

### Frontend (Capa de Presentación):
- **Angular v22** (Arquitectura 100% Standalone y **Zoneless** nativo).
- **Angular Signals** (`signal`, `computed`) para el manejo reactivo del estado en memoria fina.
- **RxJS** para la orquestación asíncrona de eventos y buscadores optimizados con cancelación de peticiones en vuelo.
- **Reactive Forms** con validaciones de contratos síncronas en tiempo real.
- **Nginx** como servidor web inverso y servidor de estáticos SPA optimizado.

---

## 🚀 Requisitos para Ejecución Local
Únicamente se requiere tener instalado en la máquina anfitriona:
- **Docker Desktop** (con soporte para `docker compose`).

*Nota: Gracias al uso de construcciones multi-etapa (Multi-stage builds) dentro de los contenedores, **no es necesario** tener instalados Java, Maven, Node, Angular CLI ni MySQL de forma física en el sistema operativo.*

---

## ⚡ Instrucciones de Arranque (Flujo de Ejecución Rápido)

Para compilar, empaquetar, inicializar la base de datos y levantar toda la plataforma de forma automática en una red privada aislada de contenedores, ejecuta el siguiente comando en la raíz del proyecto:

```bash
docker compose up --build
```

Una vez que el proceso de compilación de Docker finalice, el ecosistema estará expuesto en los siguientes accesos locales:
- **Frontend Client (Interfaz Web):** `http://localhost:4200`
- **Backend API REST:** `http://localhost:8080`
- **Base de Datos Relacional (MySQL):** `localhost:3306`

---

## 🔐 Cuentas de Prueba Pre-configuradas
El sistema incluye un sembrador automático de datos (`DataInitializer`) que inyecta y cifra mediante **BCrypt** dos perfiles de prueba en la base de datos de manera nativa al arrancar:

1. **Perfil Cliente (USER):**
   - **Email:** `usuario@test.com`
   - **Contraseña:** `123`
   - **Flujo:** Permite simular el cálculo de intereses reactivo mediante `computed()`, enviar solicitudes de crédito validadas, consultar el historial del cliente y ver el total acumulado de préstamos.

2. **Perfil Administrador (ADMIN):**
   - **Email:** `admin@test.com`
   - **Contraseña:** `123`
   - **Flujo:** Permite el uso de buscadores reactivos con cancelación asíncrona en vuelo mediante `switchMap`, auditar todas las solicitudes del banco globales y ejecutar transacciones atómicas seguras para **Aprobar** o **Rechazar** los préstamos en estado `PENDING`.
