# Microservicios Banco

Sistema bancario basico hecho con Java Spring Boot usando una arquitectura de microservicios. En este proyecto manejo clientes, cuentas, movimientos y reportes.

El sistema esta dividido en dos microservicios:

- cliente-service: maneja personas y clientes.
- cuenta-service: maneja cuentas, movimientos y reportes.

Los dos servicios se comunican por RabbitMQ y usan PostgreSQL como base de datos. Todo el proyecto esta dockerizado con Docker Compose.

## Stack

Backend:

- Java 21
- Spring Boot 3.2
- Spring Data JPA
- PostgreSQL
- RabbitMQ
- Swagger
- Lombok
- JUnit 5
- Docker Compose

## Como correrlo

Desde la raiz del proyecto ejecuto:

```bash
docker compose up --build
```

Esto levanta PostgreSQL, RabbitMQ, cliente-service y cuenta-service.

Despues de levantarlo, puedo entrar a:

```text
cliente-service: http://localhost:8081/api/clientes
cuenta-service: http://localhost:8082/api/cuentas
RabbitMQ: http://localhost:15672
Swagger cliente: http://localhost:8081/swagger-ui.html
Swagger cuenta: http://localhost:8082/swagger-ui.html
```

Credenciales de RabbitMQ:

```text
usuario: guest
clave: guest
```

Para apagar los contenedores:

```bash
docker compose down
```

Para apagar y borrar tambien la data:

```bash
docker compose down -v
```

Si quiero levantar todo desde cero:

```bash
docker compose down -v
docker compose up --build
```

## Base de datos

El proyecto usa PostgreSQL. La base de datos y la data inicial estan en el archivo:

```text
BaseDatos.sql
```

Ese archivo crea las bases, las tablas y la data inicial para probar el sistema.

La data principal incluye estos clientes:

- Carlos Ramirez
- Maria Gonzalez
- Juan Perez

Tambien se cargan cuentas y movimientos de ejemplo.

## Microservicios

### cliente-service

Este servicio se encarga de manejar los clientes y personas.

Puerto:

```text
8081
```

Base principal:

```text
clientes_db
```

Endpoints principales:

```text
GET    /api/clientes
GET    /api/clientes/{clienteId}
POST   /api/clientes
PUT    /api/clientes/{clienteId}
DELETE /api/clientes/{clienteId}
```

Cuando creo, actualizo o elimino un cliente, este servicio envia un evento a RabbitMQ.

### cuenta-service

Este servicio se encarga de manejar cuentas, movimientos y reportes.

Puerto:

```text
8082
```

Base principal:

```text
cuentas_db
```

Endpoints principales:

```text
GET    /api/cuentas
GET    /api/cuentas/{numeroCuenta}
POST   /api/cuentas
PUT    /api/cuentas/{numeroCuenta}

GET    /api/movimientos
GET    /api/movimientos/{id}
POST   /api/movimientos
PUT    /api/movimientos/{id}

GET    /api/reportes?cliente={clienteId}&fechaInicio=YYYY-MM-DD&fechaFin=YYYY-MM-DD
```

Ejemplo de reporte:

```text
http://localhost:8082/api/reportes?cliente=mgonzalez&fechaInicio=2022-02-01&fechaFin=2022-02-28
```

## Comunicacion con RabbitMQ

Uso RabbitMQ para que los microservicios no dependan directamente uno del otro.

Cuando se crea o actualiza un cliente en cliente-service, se publica un evento. Luego cuenta-service escucha ese evento y guarda una copia del cliente en su tabla cliente_ref.

Con eso, cuenta-service puede crear cuentas y generar reportes sin tener que llamar directamente a cliente-service.

## Reglas de negocio

Los depositos se registran con valor positivo.

Los retiros se registran con valor negativo.

Cada movimiento actualiza el saldo disponible de la cuenta.

Si intenta retirar mas dinero del que tiene la cuenta, el sistema devuelve:

```text
Saldo no disponible
```

## Postman

La coleccion de Postman esta en:

```text
Banco.postman_collection.json
```

La puede importar en Postman y probar los endpoints.

Orden recomendado para probar:

```text
1. Clientes
2. Cuentas
3. Movimientos
4. Reportes
5. Casos de error
```

## Swagger

Tambien puede probar los endpoints desde Swagger:

```text
http://localhost:8081/swagger-ui.html
http://localhost:8082/swagger-ui.html
```

Si esas rutas no abren, puede usar:

```text
http://localhost:8081/swagger-ui/index.html
http://localhost:8082/swagger-ui/index.html
```

## Tests

Para correr los tests de cliente-service:

```bash
cd cliente-service
mvn test
```

Para correr los tests de cuenta-service:

```bash
cd cuenta-service
mvn test
```

## Estructura

```text
microservicios-banco/
  docker-compose.yml
  BaseDatos.sql
  Banco.postman_collection.json
  README.md

  cliente-service/
    Dockerfile
    pom.xml
    src/

  cuenta-service/
    Dockerfile
    pom.xml
    src/
```

## Notas

Use una separacion sencilla por capas para mantener el codigo ordenado.

Cada microservicio tiene su propia responsabilidad.

cliente-service maneja clientes.

cuenta-service maneja cuentas, movimientos y reportes.

RabbitMQ se usa para enviar eventos entre servicios.

PostgreSQL guarda la informacion del sistema.

Docker Compose permite levantar todo con un solo comando.
