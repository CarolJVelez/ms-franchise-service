# ms-franchise-service
# ms-franchise-service

API desarrollada en Spring Boot para gestionar franquicias, sucursales y productos.

## Descripción

La aplicación permite:

- Crear una franquicia
- Agregar una sucursal a una franquicia
- Agregar un producto a una sucursal
- Eliminar un producto de una sucursal
- Actualizar el stock de un producto
- Consultar el producto con mayor stock por sucursal para una franquicia
- Actualizar el nombre de la franquicia
- Actualizar el nombre de la sucursal
- Actualizar el nombre del producto

## Tecnologías utilizadas

- Java 17
- Spring Boot 3
- Spring WebFlux
- Spring Data Reactive MongoDB
- MongoDB
- Docker
- Docker Compose
- OpenAPI / Swagger

## Arquitectura

El proyecto sigue una arquitectura hexagonal, separando:

- `domain`: modelos, puertos y casos de uso
- `infrastructure/adapters`: persistencia MongoDB
- `infrastructure/entrypoints`: controladores REST, handlers, DTOs y mappers
- `application/config`: configuración de beans

## Requisitos previos

- Java 17
- Docker
- Docker Compose

## Configuración

La aplicación utiliza la variable de entorno:

- `SPRING_DATA_MONGODB_URI`

Valor por defecto recomendado para ejecución local:

```yaml
mongodb://localhost:27017/franchise_db
```
## Ejecución local con Docker Compose

Primero asegúrate de que el docker-compose.yaml tenga configurado el servicio de la aplicación con build.

Luego ejecuta:

docker compose up -d --build

## Ejecución local con Docker Compose

Primero asegúrate de que el docker-compose.yaml tenga configurado el servicio de la aplicación con build.

Luego ejecuta:

docker compose up -d --build

Esto levantará:

MongoDB en el puerto 27017
La aplicación en el puerto 8086


## Ejecución local sin Docker para la app

Puedes levantar solo MongoDB con Docker:

docker compose up -d mongodb

Y luego ejecutar la aplicación localmente:

./gradlew bootRun
URL base
http://localhost:8086


## Swagger

Si la aplicación inicia correctamente, la documentación estará disponible en una de estas rutas:

http://localhost:8086/webjars/swagger-ui/index.html