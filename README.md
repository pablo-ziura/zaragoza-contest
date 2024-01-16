# ZaraTrivia

<p align="center">
  <img src="app/src/main/ic_launcher-playstore.png" alt="Logo de ZaraTrivia" width="200">
</p>

## Descripción General

ZaraTrivia es una aplicación interactiva de preguntas y respuestas diseñada para poner a prueba tus conocimientos sobre la histórica ciudad de Zaragoza. Con un enfoque en la diversión y el aprendizaje, cada pregunta acertada te acerca más a convertirte en un experto de la ciudad.

## Introducción

Esta aplicación ofrece una manera única de explorar Zaragoza, desafiando a los usuarios con preguntas de opción múltiple y una ronda especial donde deben ubicar algún lugar famoso en el mapa de la ciudad. Con un diseño atractivo y una interfaz intuitiva, ZaraTrivia hace que el aprendizaje sobre Zaragoza sea entretenido para todos.

## Features

- **Preguntas Dinámicas**: Una amplia variedad de preguntas que se actualizan regularmente.
- **Puntuación por Tiempo**: Los usuarios obtienen más puntos cuanto más rápido responden.
- **Ronda de Bonus en el Mapa**: Utiliza Google Maps para una experiencia interactiva.
- **Autenticación de Usuarios**: Ingreso seguro a través de Firebase.
- **Tabla de Puntuaciones**: Tablas de clasificación para fomentar la competencia amistosa entre los usuarios.
- **Diseño Responsivo**: Optimizada para una variedad de dispositivos móviles.

## Capturas de pantalla

<p align="center">
  <img src="assets/img/screenshots/screen_001.png" alt="Screenshot 01" width="150">
  <img src="assets/img/screenshots/screen_002.png" alt="Screenshot 02" width="150">
  <img src="assets/img/screenshots/screen_003.png" alt="Screenshot 03" width="150">
  <img src="assets/img/screenshots/screen_004.png" alt="Screenshot 04" width="150">
  <img src="assets/img/screenshots/screen_005.png" alt="Screenshot 05" width="150">
</p>

## Estructura del Proyecto

La aplicación ZaraTrivia sigue una **Arquitectura Clean** y **principios SOLID**, y está organizada en las siguientes carpetas de alto nivel dentro de la carpeta `src/main/java/`:

- `data/` - Contiene las implementaciones concretas de la lógica de acceso a datos, separadas en fuentes de datos remotas y preferencias locales.
  - `question/` - Operaciones relacionadas con las preguntas del trivia.
  - `score/` - Operaciones relacionadas con el manejo de puntuaciones.
  - `user/` - Operaciones relacionadas con la información de los usuarios.
  - `remote/` - Implementaciones específicas para la comunicación con servicios remotos, como Firebase.
- `storage/` - Implementaciones para el almacenamiento local, usando preferencias compartidas o bases de datos locales.
- `di/` - Configuración de la inyección de dependencias para la aplicación, utilizando Koin.
- `domain/` - Contiene los casos de uso y la lógica de negocio, asegurando la separación de la lógica de la aplicación y la UI.
- `usecase/` - Casos de uso específicos para manejar las acciones del usuario y la comunicación con el repositorio de datos.
- `repository/` - Interfaces de los repositorios que abstraen el origen de los datos.
- `model/` - Definiciones de los objetos de dominio que representan las entidades del negocio.
- `ui/` - Todos los componentes de la interfaz de usuario, organizados por funcionalidad y pantallas.
- `common/` - Componentes de UI compartidos y utilizados en varias partes de la aplicación.
- `fragment/` - Fragmentos que representan las distintas vistas dentro de la aplicación.
- `theme/` - Definiciones de estilo y temática para mantener un diseño coherente a través de la aplicación.
- `viewmodel/` - ViewModels que siguen el patrón MVVM, proporcionando los datos y la lógica para las vistas.
- `utils/` - Funciones y clases de utilidad que proporcionan servicios transversales como formateadores, validadores o extensiones de Kotlin.
- `ContestApplication.kt` - Punto de entrada de la aplicación y de inyección de dependencias.

## Librerías

Para el diseño de una aplicación robusta y escalable, se han utlizado las siguientes librerías en el proyecto:

- [Flutter](https://flutter.dev) - El SDK de UI para crear hermosas aplicaciones compiladas nativamente.
- [Dio](https://github.com/flutterchina/dio) - Un cliente HTTP dinámico y potente para Dart, que facilita el manejo de solicitudes y respuestas.
- [SharedPreferences](https://pub.dev/packages/shared_preferences) - Una solución para almacenar datos de clave-valor de forma persistente, ideal para preferencias de usuario y configuraciones simples.
- [GetIt](https://pub.dev/packages/get_it) - Un localizador de servicios para Dart y Flutter, que se utiliza para la inyección de dependencias.
- [go_router](https://pub.dev/packages/go_router) - Un enrutador declarativo para Flutter que facilita la navegación y la gestión de rutas.

Estas herramientas y librerías se han seleccionado cuidadosamente para trabajar juntas y proporcionar una base sólida y flexible para la **arquitectura MVVM**, facilitando un código mantenible y una experiencia de usuario fluida.

## Instalación

Sigue estos pasos para configurar el entorno de desarrollo y ejecutar la aplicación:

### Clona el repositorio de GitHub
git clone https://github.com/pablo-ziura/zaragoza-contest.git

### Navega al directorio del proyecto clonado
cd ultimate-movie-database

### Instala las dependencias del proyecto
flutter pub get

### Ejecuta la aplicación en modo de desarrollo
flutter run

### Registro y Uso de la API

Para utilizar la API de TMDB en tu propio entorno de desarrollo, necesitarás registrarte en [The Movie Database (TMDB)](https://developer.themoviedb.org/docs/getting-started) y obtener tu propia clave API.

## Play Store

Puedes instalar y ejecutar la aplicación desde la Play Store: [Ficha Play Store](https://play.google.com/store/apps/details?id=com.zaragoza.contest)

## Licencia

Este proyecto está licenciado bajo la Licencia MIT - ver el archivo [LICENSE.md](LICENSE.md) para detalles.