# Laboratorio 1: Interfaz de Usuario e Input Controls

Este proyecto es una aplicación de Android desarrollada para la asignatura de **Computación Móvil**. La aplicación se enfoca en la implementación de una interfaz de usuario moderna utilizando **Jetpack Compose** y **Material Design 3**, centrada en la captura y validación de datos de usuario.

# Integrantes

Jhon Deivy Rodas Morales
Nelson Alcides Puerta Garcia

## 🚀 Características

La aplicación consta de dos pantallas principales de captura de datos:

### 1. Información Personal (`PersonalDataActivity`)
- **Nombres y Apellidos:** Campos de texto con validación de obligatoriedad.
- **Sexo:** Selección mediante `RadioButtons` (Hombre/Mujer).
- **Fecha de Nacimiento:** Implementación de `DatePicker` de Material 3 para una selección intuitiva.
- **Grado de Escolaridad:** Menú desplegable (`ExposedDropdownMenu`) con opción "Otro" que habilita un campo de texto dinámico.
- **Soporte Landscape:** Diseño adaptativo que utiliza `Row` en modo horizontal y `Column` en vertical.

### 2. Información de Contacto (`ContactDataActivity`)
- **Teléfono:** Teclado numérico optimizado.
- **Email:** Validación de formato y teclado de correo electrónico.
- **País:** Campo con autocompletado (`ExposedDropdownMenuBox`) que incluye países de LATAM.
- **Ciudad:** Campo con autocompletado y búsqueda filtrada que incluye las principales ciudades de Colombia.
- **Dirección:** Campo opcional para información detallada.

## 🛠️ Stack Tecnológico

- **Lenguaje:** Kotlin
- **UI Framework:** Jetpack Compose
- **Design System:** Material Design 3 (M3)
- **SDK Mínimo:** 31 (Android 12)
- **Target SDK:** 37 (Android 15)
- **Componentes:**
    - `OutlinedTextField` para entradas de datos.
    - `rememberSaveable` para persistencia de estado ante rotaciones.
    - `DatePicker` para manejo de calendarios.
    - `ExposedDropdownMenu` para listas de selección.

## 📋 Validación y Logs

La aplicación implementa una lógica de validación robusta:
1. Al presionar **Siguiente**, se verifica que todos los campos marcados con asterisco `*` no estén vacíos.
2. Si falta algún dato, se muestra un mensaje de error visual debajo del campo correspondiente.
3. Una vez validados los datos, se imprimen en el **Logcat** con el siguiente formato:

```text
Información personal:
[Nombres] [Apellidos]
[Sexo]
Nació el [DD/MM/AAAA]
[Escolaridad]

Información de contacto:
Teléfono: [Número]
Dirección: [Dirección]
Email: [Correo]
País: [País]
Ciudad: [Ciudad]
```

## ⚙️ Configuración del Proyecto

1. Abrir en **Android Studio**.
2. Sincronizar con Gradle.
3. Ejecutar en un emulador o dispositivo físico con Android 12+.

---
**Desarrollado por:** Grupo 12 - Computación Móvil  
**Universidad de Antioquia**
