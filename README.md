# AD 🐲🐲

![IMG](https://i.pinimg.com/originals/46/4f/d9/464fd9d9ba7c3450c9a8ae98f3e03362.gif)

## PASOS INICIALEESS 

>[!TIP]
> 1. ***Levantar la máquina virtual, asegurar la correcta conexión y accesibilidad con la VM, postgres y mongoDB***
> 2. ***Lanzar el script de creación de tablas para postgreSQL***
> 3. ***Configuración del application.properties***

### CONFIGURACIÓN DE PELISPOSTGRES 🎨

> [!NOTE]
>  ***Empezamos primero a configurar PelisPostgres***

#### Esctructura SQL

```SQL
CREATE TABLE peliculas (
    idPelicula SERIAL PRIMARY KEY, -- IDENTIFICADOR ÚNICO AUTOINCREMENTAL QUE ACTÚA COMO CLAVE PRIMARIA [cite: 12]
    titulo VARCHAR(150),           -- CADENA DE TEXTO PARA EL TÍTULO DE LA PELÍCULA CON UN MÁXIMO DE 150 CARACTERES [cite: 13]
    xenero VARCHAR(50),            -- CAMPO PARA DEFINIR EL GÉNERO CINEMATOGRÁFICO [cite: 14]
    ano INT                        -- VALOR NUMÉRICO ENTERO PARA REPRESENTAR EL AÑO DE LANZAMIENTO [cite: 15]
);

CREATE TABLE actores (
    idActor SERIAL PRIMARY KEY,    -- IDENTIFICADOR ÚNICO AUTOINCREMENTAL PARA CADA ACTOR [cite: 18]
    nome VARCHAR(100),             -- NOMBRE DEL ACTOR O ACTRIZ [cite: 19]
    apelidos VARCHAR(100),         -- APELLIDOS DEL ACTOR O ACTRIZ [cite: 20]
    nacionalidade VARCHAR(100),    -- PAÍS DE ORIGEN DEL ACTOR [cite: 21]
    id_pelicula INT REFERENCES peliculas (idPelicula) -- CLAVE FORÁNEA QUE CONECTA AL ACTOR CON UNA PELÍCULA ESPECÍFICA [cite: 22]
);
```

#### Application.properties

```java
app.version=1.0.0

# CONFIGURACIÓN DE LA URL DE CONEXIÓN A LA BASE DE DATOS POSTGRESQL EN LA MÁQUINA VIRTUAL 
spring.datasource.url=jdbc:postgresql://192.168.1.61:5432/"nombre de tu base de datos"

# NOMBRE DE USUARIO PARA ACCEDER AL GESTOR DE BASE DE DATOS POSTGRESQL 
spring.datasource.username=postgres

# CONTRASEÑA ASOCIADA AL USUARIO POSTGRES PARA LA AUTENTICACIÓN 
spring.datasource.password=admin

# ESPECIFICAMOS EL DRIVER DE POSTGRESQL PARA QUE JAVA SEPA CÓMO COMUNICARSE CON LA DB 
spring.datasource.driver-class-name=org.postgresql.Driver

# INDICAMOS EL DIALECTO DE HIBERNATE ESPECÍFICO PARA POSTGRESQL PARA OPTIMIZAR LAS CONSULTAS SQL 
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect

# ACTIVAMOS LA VISUALIZACIÓN DE LAS CONSULTAS SQL EN LA CONSOLA PARA FACILITAR EL DEPURADO DURANTE EL DESARROLLO 
spring.jpa.show-sql=true

# ASIGNAMOS UN PUERTO PARA ESTE MICROSERVICIO 
server.port=8085
```





