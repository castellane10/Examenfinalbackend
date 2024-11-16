# Backend - Consumo de Servicio SOAP y Exposición REST

## Descripción

Este componente es un backend desarrollado con Spring Boot que consume un servicio SOAP proporcionado por el Banco de Guatemala para obtener el tipo de cambio de moneda a quetzales. 

## Requisitos Previos

- **Java JDK** (versión: 17)
- **Maven** (para gestionar dependencias y construir el proyecto)
- **Base de datos** H2


## Instalación

Clona el repositorio del backend:

git clone <https://github.com/castellane10/Examenfinalbackend>


# Construcción y Ejecución
Construye el proyecto con Maven
./mvnw clean install

# Ejecuta la aplicación:
./mvnw spring-boot:run

# El backend estará disponible en la siguiente URL de la API:
http://localhost:8080/api/soap/tipocambio
Cada vez que accedas a esta URL con un método GET, se obtendrá el tipo de cambio y se guardará un registro en la base de datos.

# Consola H2 (opcional)
Para acceder a la consola de la base de datos H2, utiliza la siguiente URL:
http://localhost:8080/h2-console
Las credenciales para la consola H2 están en el archivo application.properties.

# Funcionamiento con frontend
Mantener ejecutado el proyecto del backend para consumir la Api en el proyecto del frontend.