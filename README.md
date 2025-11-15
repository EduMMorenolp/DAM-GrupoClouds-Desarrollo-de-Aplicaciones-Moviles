## 📄 DAM - Club Deportivo (GrupoClouds)

### ⚽ Club Deportivo - Aplicación de Gestión Móvil

Este proyecto corresponde a la implementación de la aplicación móvil del **Sistema de Gestión del Club Deportivo**, desarrollado en el marco de la asignatura **Desarrollo de Aplicaciones para Dispositivos Móviles (DAM)**.

El objetivo es migrar la funcionalidad principal del sistema de escritorio preexistente (DSOO/MDS) a una plataforma **Android nativa**, adoptando una arquitectura moderna y una experiencia de usuario optimizada para móviles (UI/UX).

-----

### 💻 Tecnología y Stack

| Componente | Tecnología | Propósito |
| :--- | :--- | :--- |
| **Plataforma** | Android | Desarrollo nativo. |
| **Lenguaje** | **Kotlin** | Lenguaje moderno, seguro y recomendado por Google. |
| **IDE** | **Android Studio** | Entorno de desarrollo. |
| **Persistencia Local** | **Room** | Librería de persistencia para DB local, integrada con MVVM. |
| **Asincronía** | Kotlin Coroutines & Flow | Manejo de operaciones en segundo plano sin bloquear la UI. |
| **Backend (Futuro)** | MySQL / API RESTful | Base de datos principal para el sistema de producción. |
| **Control de Versiones** | Git / GitHub | Gestión de ramas y colaboración del equipo. |

-----

### ✨ Avance y Estado de Funcionalidades

**El diseño UI/UX está completado.** La fase actual se centra en la implementación de la lógica de negocio (Backend/Persistencia). El estado del desarrollo es el siguiente:

| Módulo | Descripción | Estado |
| :--- | :--- | :--- |
| **Diseño UI/UX** | Todas las Activities y Vistas diseñadas en XML. | **✅ COMPLETADO** |
| **Autenticación** | Login para el personal administrativo y de recepción. | **✅ COMPLETADO** |
| **Cuotas y Pagos** | Lógica de registro de pagos y consulta de vencimientos. | **✅ COMPLETADO** |
| **Actividades** | Lógica de Alta, Baja y Modificación (CRUD) de actividades. | **✅ COMPLETADO** |
| **Gestión de Socios** | **Registro** de nuevas personas (socios/no socios). | **✅ COMPLETADO** |
| **Gestión de Socios** | **Consulta de Fichas** de socios existentes. | **✅ COMPLETADO** |
| **Carnet Digital** | Generación y visualización del carnet de socio. | **✅ COMPLETADO** |

-----

### 🚀 Estructura del Proyecto (MVVM & Room)

El proyecto sigue la arquitectura MVVM para una separación de responsabilidades limpia.

| Componente | Rol en el Proyecto |
| :--- | :--- |
| **DAO** (Data Access Object) | Interfaces de Room con las consultas SQL para la DB. |
| **Entidades** | *Data Classes* que representan las tablas de la DB (`Socio`, `Administrador`, `Cuota`). |
| **ViewModel** | Contiene la lógica de negocio y prepara los datos para la UI. |
| **Activities** | Contiene la Vista (UI) y observa los datos del ViewModel. |

#### Activities Principales Implementadas:

  * `LoginActivity`
  * `DashboardActivity`
  * `SocioRegistroActivity`
  * `PagosActivity`
  * `ActividadesActivity`
  * `CuotasVencidasActivity`

-----

### 🛠️ Cómo Iniciar el Proyecto (Prerequisitos)

Para clonar y ejecutar este proyecto en tu entorno local, necesitarás:

1.  Tener instalado **Git**.
2.  Tener instalado **Android Studio** (versión recomendada: la más reciente) con el SDK de Android configurado.

#### 1\. Clonar el Repositorio

Abre tu terminal o Git Bash y ejecuta:

bash
git clone [https://github.com/EduMMorenolp/DAM-GrupoClouds-Desarrollo-de-Aplicaciones-Moviles.git](https://github.com/EduMMorenolp/DAM-GrupoClouds-Desarrollo-de-Aplicaciones-Moviles.git)

#### 2\. Abrir en Android Studio

1.  Abre **Android Studio**.
2.  Selecciona **Open an existing Android Studio project**.
3.  Navega a la carpeta `DAM-GrupoClouds-Desarrollo-de-Aplicaciones-Moviles` y ábrela.
4.  Espera a que Gradle sincronice el proyecto.

#### 3\. Ejecutar la Aplicación

1.  Selecciona un dispositivo emulado (AVD) o conecta un dispositivo físico.
2.  Haz clic en el botón **Run** (el ícono de flecha verde).

-----

### 👥 Equipo de Desarrollo

Este proyecto es desarrollado por el **Grupo Clouds**.

  * **Eduardo Moreno**
  * **Leandro Paryszewski**
  * **Marcelo Moreno**
  * **Melissa Galeano Ibañez**

-----

### 📌 Estado Actual y Próximos Pasos

📌 Próximos Pasos (Foco Inmediato)

El enfoque actual es completar las funcionalidades que dependen de consultas avanzadas:

Implementar la Consulta de Fichas de socios (búsqueda y visualización completa de datos).

Implementar la lógica completa del Carnet Digital (visualización, generación de PDF y envío por email).
