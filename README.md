# 📓 Mis Notas de Campo

> **Tu bitácora digital.** Una aplicación Android nativa para registrar hallazgos, tareas o recordatorios visuales directamente en "el campo".

![Android](https://img.shields.io/badge/Platform-Android-3DDC84.svg?style=flat&logo=android)
![Kotlin](https://img.shields.io/badge/Language-Kotlin-7F52FF.svg?style=flat&logo=kotlin)
![License](https://img.shields.io/badge/License-MIT-blue.svg)

## 📋 Descripción

**Mis Notas de Campo** es una herramienta sencilla pero potente diseñada para capturar información rápida. Permite a los usuarios crear notas que incluyen un título, una descripción detallada y, lo más importante, una **fotografía real** tomada en el momento.

A diferencia de aplicaciones complejas en la nube, esta app está diseñada para funcionar **100% offline**, almacenando los datos de forma segura en el dispositivo mediante serialización de objetos.

## ✨ Funcionalidades Principales

* **📝 Crear Notas:** Formulario intuitivo para ingresar título y descripción (multilínea).
* **📷 Integración con Cámara:** Captura fotos directamente desde la app para adjuntarlas a tus notas (usa `ActivityResultContracts`).
* **💾 Persistencia Local:** Las notas y fotos se guardan en el almacenamiento interno del dispositivo (`notas_guardadas.bin`), asegurando que la información perdure aunque cierres la app.
* **👀 Visualización de Detalle:** Pantalla dedicada para revisar la información y ver la foto en tamaño grande.
* **🗑️ Gestión de Notas:**
    * **Clic corto:** Ver detalle de la nota.
    * **Clic largo:** Eliminar la nota permanentemente.
* **👋 Flujo de Bienvenida:** Pantallas de Splash y Bienvenida amigables.

## 🛠️ Stack Tecnológico

El proyecto está construido siguiendo las prácticas estándar de desarrollo Android moderno:

* **Lenguaje:** [Kotlin](https://kotlinlang.org/) (v2.0+)
* **UI:** XML Layouts (Diseño clásico con `LinearLayout`, `ScrollView`, `Spinner`).
* **SDK Mínimo:** API 24 (Android 7.0).
* **SDK Objetivo:** API 36 (Android 16 - Preview/Latest).
* **Almacenamiento:**
    * Serialización Java (`Serializable`, `ObjectOutputStream`) para los metadatos.
    * Sistema de archivos interno (`openFileOutput`) para las imágenes.
* **Navegación:** Intents explícitos.
* **Build System:** Gradle (Kotlin DSL).

## 📂 Estructura del Proyecto

Los archivos principales del código fuente se encuentran en `com.example.misnotasdecampo`:

* `MainActivity.kt`: Pantalla principal que lista todas las notas guardadas.
* `CreateNoteActivity.kt`: Lógica para capturar la foto y guardar la nueva nota.
* `DetailActivity.kt`: Muestra la información completa y permite navegar entre notas usando un `Spinner`.
* `DatosNotas.kt`: Contiene el modelo de datos (`data class Nota`) y el objeto `AlmacenNotas` encargado de la persistencia (Guardar/Cargar).
* `SplashActivity.kt` & `WelcomeActivity.kt`: Pantallas de introducción.

## 🚀 Instalación y Uso

1.  **Clonar el repositorio:**
    ```bash
    git clone [https://github.com/Dn-este/MisNotasDeCampo.git](https://github.com/Dn-este/MisNotasDeCampo.git)
    ```
2.  **Abrir en Android Studio:**
    * Abre Android Studio.
    * Selecciona "Open" y busca la carpeta clonada.
    * Espera a que Gradle sincronice las dependencias.
3.  **Ejecutar:**
    * Conecta tu dispositivo Android o usa un Emulador.
    * Dale al botón de **Run (▶)**.

> **Nota:** La aplicación solicitará permisos de **Cámara** la primera vez que intentes tomar una foto.

## 📸 Capturas de Pantalla (Preview)

*(Aquí puedes subir capturas reales de tu app funcionando)*

| Lista de Notas | Creación de Nota | Detalle |
|:---:|:---:|:---:|
| *(Imagen de Main)* | *(Imagen de Create)* | *(Imagen de Detail)* |

## Contribución

Este es un proyecto de código abierto y las contribuciones son bienvenidas. Si tienes ideas para mejorar la persistencia (ej. migrar a Room Database) o mejorar la UI:

1.  Haz un Fork.
2.  Crea tu rama (`git checkout -b feature/AmazingFeature`).
3.  Haz Commit (`git commit -m 'Add some AmazingFeature'`).
4.  Push a la rama (`git push origin feature/AmazingFeature`).
5.  Abre un Pull Request.

## 📄 Licencia

Distribuido bajo la licencia MIT. Ver `LICENSE` para más información.

---
Hecho por [Dn-este](https://github.com/Dn-este)
