# Frontend - Bank Loan System Client

This project was generated using [Angular CLI](https://github.com/angular/angular-cli) version 22.1.8. 

Esta aplicación web SPA independiente implementa la interfaz de interacción para clientes y administradores del sistema de gestión de préstamos bancarios, desarrollada bajo los estándares modernos de rendimiento y reactividad fina.

## 🏗️ Arquitectura & Decisiones de Diseño (Sustentación Senior)

- **Arquitectura Feature-Based Zoneless:** La aplicación prescinde por completo de la librería tradicional `zone.js` (Zoneless nativo), reduciendo drásticamente el overhead del ciclo de vida en el navegador. El enrutamiento y renderizado dinámico se orquestan mediante componentes independientes y lazy loading en `app.routes.ts`.
- **Manejo de Estado Fino con Signals:** Se descartó el uso de NgRx por sobreingeniería para un flujo transaccional directo. Se optó por **Angular Signals** en los componentes y servicios (`currentUser`, `loans`) para notificar mutaciones síncronas de datos de forma quirúrgica en el DOM.
- **Optimización de Cálculos Derivados:** Se aplicó la primitiva **`computed()`** para la generación de totales acumulados y simuladores de tasas de interés en tiempo real. Al ser memorizados, estos valores solo se recalculan si su señal de dependencia cambia directamente.
- **Buscadores Reactivos Asíncronos (RxJS):** En el panel del administrador se implementó un `Subject` conectado a un flujo reactivo que utiliza `debounceTime(300)`, `distinctUntilChanged()` y **`switchMap()`**. Esto garantiza la cancelación de peticiones HTTP en vuelo si el usuario sigue escribiendo, optimizando el ancho de banda y protegiendo el backend.
- **Protección de Rutas e Intercepción Funcional:** Se centralizó la inyección del JWT utilizando un interceptor funcional moderno (`HttpInterceptorFn`) que clona las peticiones salientes para adjuntar la cabecera `Authorization: Bearer`. Los accesos a las vistas están controlados por un guardián funcional (`CanActivateFn`) acoplado a los roles lógicos de la sesión.

## 🛠️ Stack Tecnológico Frontend
- **Framework:** Angular v22 (Standalone Architecture)
- **Compilador/Manejador:** Vite + NPM
- **Reactividad:** Angular Signals (Estado) + RxJS (Flujos asíncronos)
- **Formularios:** Reactive Forms (Validaciones síncronas)
- **Test Runner:** Vitest

---

## Development server

To start a local development server, run:

```bash
ng serve
```

Once the server is running, open your browser and navigate to `http://localhost:4200/`. The application will automatically reload whenever you modify any of the source files.

## Code scaffolding

Angular CLI includes powerful code scaffolding tools. To generate a new component, run:

```bash
ng generate component component-name
```

For a complete list of available schematics (such as `components`, `directives`, or `pipes`), run:

```bash
ng generate --help
```

## Building

To build the project run:

```bash
ng build
```

This will compile your project and store the build artifacts in the `dist/` directory. By default, the production build optimizes your application for performance and speed.

## Running unit tests

To execute unit tests with the [Vitest](https://vitest.dev/) test runner, use the following command:

```bash
ng test
```

## Running end-to-end tests

For end-to-end (e2e) testing, run:

```bash
ng e2e
```

Angular CLI does not come with an end-to-end testing framework by default. You can choose one that suits your needs.

## Additional Resources

For more information on using the Angular CLI, including detailed command references, visit the [Angular CLI Overview and Command Reference](https://angular.dev/tools/cli) page.
