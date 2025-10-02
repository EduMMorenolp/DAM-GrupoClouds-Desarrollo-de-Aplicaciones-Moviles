## 📄 DAM - Club Deportivo (GrupoClouds)

### ⚽ Club Deportivo - Aplicación de Gestión Móvil

Este proyecto corresponde a la implementación de la aplicación móvil del **Sistema de Gestión del Club Deportivo**, desarrollado en el marco de la asignatura **Desarrollo de Aplicaciones para Dispositivos Móviles (DAM)**.

El objetivo es migrar la funcionalidad principal del sistema de escritorio preexistente (DSOO/MDS) a una plataforma **Android nativa**, adoptando una arquitectura moderna y una experiencia de usuario optimizada para móviles (UI/UX).

-----

### 💻 Tecnología y Stack

  * **Plataforma de Desarrollo:** Android
  * **Lenguaje de Programación:** **Kotlin**
  * **IDE:** **Android Studio**
  * **Gestión de Dependencias:** Gradle
  * **Base de Datos (Backend):** MySQL (a través de una futura API RESTful)
  * **Control de Versiones:** Git / GitHub

-----

### ✨ Funcionalidades Clave

La aplicación está diseñada para automatizar y gestionar las operaciones esenciales del club, enfocándose en la administración del staff.

| Módulo | Descripción | Estado (Etapa Actual: Diseño UI) |
| :--- | :--- | :--- |
| **Autenticación** | Login para el personal administrativo y de recepción. | 🏗️ En Desarrollo (Diseño de pantallas) |
| **Gestión de Socios** | Registro de nuevas personas (socios/no socios) y consulta de fichas. | 🏗️ En Desarrollo |
| **Cuotas y Pagos** | Registro de pagos de cuotas mensuales y actividades; consulta de cuotas vencidas. | 🏗️ En Desarrollo |
| **Actividades** | Alta, baja y modificación de actividades deportivas y sus costos. | 🏗️ En Desarrollo |
| **Carnet Digital** | Generación y visualización del carnet de socio (digital). | 💡 Planificado |

-----

### 🚀 Estructura del Proyecto (Etapa de Desarrollo)

La etapa actual se centra en la implementación del diseño UI/UX. La estructura del proyecto en Android Studio incluye varias `Activity`s, cada una representando una pantalla única en la aplicación.

  * `MainActivity` (o **Entry Point**): [Indicar la pantalla de inicio, ej: SplashScreen o LoginActivity]
  * `LoginActivity`: Pantalla de autenticación de administradores.
  * `DashboardActivity`: Dashboard principal después del login.
  * `SocioRegistroActivity`: Formulario para el alta de nuevos socios/personas.
  * `PagosActivity`: Pantalla para registrar pagos de cuotas.
  * `CuotasVencidasActivity`: Listado de socios con pagos pendientes.
  * *y las demás Activities necesarias según el diseño...*

-----

### 🛠️ Cómo Iniciar el Proyecto (Prerequisitos)

Para clonar y ejecutar este proyecto en tu entorno local, necesitarás:

1.  Tener instalado **Git**.
2.  Tener instalado **Android Studio** (versión recomendada: la más reciente) con el SDK de Android configurado.

#### 1\. Clonar el Repositorio

Abre tu terminal o Git Bash y ejecuta:

```bash
git clone https://github.com/EduMMorenolp/DAM-GrupoClouds-Desarrollo-de-Aplicaciones-Moviles.git
```

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

El enfoque actual es la **Etapa Desarrollo (Diseño UI/UX)**, asegurando que todas las pantallas estén correctamente diseñadas en XML y el flujo de navegación entre `Activities` esté definido.

  * **Siguiente Etapa:** Implementación de la **Lógica de Negocio** y la integración con el **Backend (API REST)** para la gestión de datos.
