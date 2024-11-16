# Backend - Consumo de Servicio SOAP y Exposición REST

## Descripción

Este componente es un backend desarrollado con Spring Boot que consume un servicio SOAP proporcionado por el Banco de Guatemala para obtener el tipo de cambio de moneda a quetzales. 

## Requisitos Previos

- **Java JDK** (versión recomendada: 17 o superior)
- **Maven** (para gestionar dependencias y construir el proyecto)
- **Base de datos** MySQL o PostgreSQL
- **Un IDE para Java** (como IntelliJ IDEA o Eclipse)
- **Postman** (opcional, para probar los endpoints)

## Instalación

Clona el repositorio del backend:

git clone <https://github.com/castellane10/Examenfinalbackend>
cd <Examenfinalbackend>

# Configuración
Asegúrate de configurar correctamente las credenciales de la base de datos en el archivo application.properties.

# Construcción y Ejecución
Construye el proyecto con Maven
mvn clean install

# Ejecuta la aplicación:
mvn spring-boot:run

# El backend estará disponible en la siguiente URL de la API:
http://localhost:8080/api/soap/tipocambio
Cada vez que accedas a esta URL con un método GET, se obtendrá el tipo de cambio y se guardará un registro en la base de datos.

# Consola H2 (opcional)
Para acceder a la consola de la base de datos H2, utiliza la siguiente URL:
http://localhost:8080/h2-console
Las credenciales para la consola H2 están en el archivo application.properties.

