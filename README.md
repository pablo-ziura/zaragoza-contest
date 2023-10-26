# ZaraTrivia

ZaraTrivia es una aplicación de preguntas y respuestas sobre la ciudad de Zaragoza

## Tabla de contenidos

- [Estructura del Proyecto](#estructura-del-proyecto)
- [Instalación](#instalación)
- [Uso](#uso)
- [Licencia](#licencia)

## Estructura del Proyecto

El proyecto sigue una arquitectura basada en Clean Architecture y MVVM. Aquí una descripción general:

### Capa de Datos
- **data**: Contiene la lógica relacionada con la obtención de datos.
  - `question`: Datos de preguntas.
  - `score`: Datos de puntuaciones.
  - `user`: Datos de usuarios.

### Dominio
- **domain**: Núcleo de la aplicación sin dependencias externas.
  - `usecase`: Casos de uso de la aplicación.

### Capa de UI
- **ui**: Contiene la lógica de la interfaz de usuario.
  - `common`: Componentes comunes, como estados de recursos.
  - `fragment`: Fragmentos que componen diferentes secciones de la app.
  - `theme`: Elementos relacionados con el tema visual.
  - `viewmodel`: Modelos de vista para la gestión de datos y lógica de UI.

## Instalación

Ficha de Play Store: https://play.google.com/store/apps/details?id=com.zaragoza.contest

## Uso

[Instrucciones sobre cómo usar la aplicación, iniciarla, navegar, etc.] -> Pendiente


## Licencia

Este proyecto está licenciado bajo la Licencia Apache 2.0. Para más detalles, consulte el archivo [LICENSE](LICENSE).
