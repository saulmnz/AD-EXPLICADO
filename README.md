# AD 

![IMG](https://i.pinimg.com/originals/46/4f/d9/464fd9d9ba7c3450c9a8ae98f3e03362.gif)

---

## PASOS INICIALES 🐲

>[!TIP]
> 1. ***Levantar la máquina virtual, asegurar la correcta conexión y accesibilidad con la VM, postgres y mongoDB***
> 2. ***Lanzar el script de creación de tablas para postgreSQL***
> 3. ***Configuración del application.properties***

---

### CONFIGURACIÓN DE PELISPOSTGRES 🎨

> [!NOTE]
>  ***Empezamos primero a configurar PelisPostgres***

#### ESTRUCTURA SQL

```SQL
CREATE TABLE peliculas (
    idPelicula SERIAL PRIMARY KEY, -- IDENTIFICADOR ÚNICO AUTOINCREMENTAL QUE ACTÚA COMO CLAVE PRIMARIA
    titulo VARCHAR(150),           
    xenero VARCHAR(50),          
    ano INT                     
);

CREATE TABLE actores (
    idActor SERIAL PRIMARY KEY,    
    nome VARCHAR(100),             
    apelidos VARCHAR(100),         
    nacionalidade VARCHAR(100),    
    id_pelicula INT REFERENCES peliculas (idPelicula) -- CLAVE FORÁNEA QUE CONECTA AL ACTOR CON UNA PELÍCULA ESPECÍFICA 
);
```

---

#### APPLICATION.PROPERTIES

```java
# CONFIGURACIÓN DE LA CONEXIÓN FÍSICA A LA BASE DE DATOS POSTGRESQL MEDIANTE EL PROTOCOLO JDBC
spring.datasource.url=jdbc:postgresql://172.20.10.2:5432/postgres 
# DEFINE LA RUTA DE ACCESO A LA BASE DE DATOS ESPECIFICANDO EL DRIVER JDBC, LA DIRECCIÓN IP DE TU MÁQUINA VIRTUAL, EL PUERTO POR DEFECTO DE POSTGRES (5432) Y EL NOMBRE DE LA BASE DE DATOS ("POSTGRES") A LA QUE SE CONECTARÁ EL MICROSERVICIO

# CREDENCIAL DE ACCESO: NOMBRE DE USUARIO PARA LA AUTENTICACIÓN EN EL MOTOR DE BASE DE DATOS
spring.datasource.username=postgres 
# ESPECIFICA EL NOMBRE DEL USUARIO ADMINISTRADOR O CON PERMISOS DE ESCRITURA DENTRO DE POSTGRESQL PARA QUE SPRING PUEDA REALIZAR LAS OPERACIONES CRUD

# CREDENCIAL DE ACCESO: CONTRASEÑA ASOCIADA AL USUARIO PARA VALIDAR LA CONEXIÓN SEGURA
spring.datasource.password=postgres 
# DEFINE LA CLAVE SECRETA QUE PERMITE AL MICROSERVICIO AUTENTICARSE CORRECTAMENTE; DEBE COINCIDIR CON LA CONFIGURADA DURANTE LA INSTALACIÓN DE POSTGRES EN TU VM

# ESPECIFICACIÓN DEL CONTROLADOR (DRIVER) NECESARIO PARA LA COMUNICACIÓN ENTRE JAVA Y POSTGRESQL
spring.datasource.driver-class-name=org.postgresql.Driver 
# INDICA A SPRING BOOT QUÉ LIBRERÍA ESPECÍFICA (EL JAR DE POSTGRES) DEBE UTILIZAR PARA TRADUCIR LAS ÓRDENES DE JAVA AL LENGUAJE QUE ENTIENDE EL SERVIDOR DE BASE DE DATOS

# CONFIGURACIÓN DEL DIALECTO DE HIBERNATE PARA LA GENERACIÓN DE CONSULTAS SQL ÓPTIMAS
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect 
# ESTA PROPIEDAD ES VITAL PORQUE LE DICE AL ORM (HIBERNATE) QUE USE LA SINTAXIS ESPECÍFICA DE POSTGRESQL AL GENERAR EL SQL AUTOMÁTICO, EVITANDO ERRORES DE COMPATIBILIDAD ENTRE DISTINTOS SISTEMAS SQL

# ESTRATEGIA DE GESTIÓN AUTOMÁTICA DEL ESQUEMA DE LA BASE DE DATOS (DDL)
spring.jpa.hibernate.ddl-auto=update 
# ESTE PARÁMETRO ES MUY ÚTIL EN DESARROLLO YA QUE HIBERNATE COMPARARÁ TUS CLASES @ENTITY (COMO PELICULA O ACTOR) CON LAS TABLAS EXISTENTES Y CREARÁ O MODIFICARÁ LAS COLUMNAS QUE FALTEN SIN BORRAR TUS DATOS PREVIOS

# DEFINICIÓN DEL PUERTO DE RED DONDE ESCUCHARÁ EL MICROSERVICIO PELISPOSTGRES
server.port=8085 
# ESTABLECE QUE EL MICROSERVICIO ESTARÁ DISPONIBLE EN EL PUERTO 8085; RECUERDA QUE TODAS LAS LLAMADAS DESDE EL NAVEGADOR O DESDE EL OTRO MICROSERVICIO (MONGOCHAMADOR) DEBERÁN APUNTAR EXACTAMENTE A ESTE NÚMERO
```

---


#### ENTIDADES Pelicula.java y Actor.java

```java
package org.example.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "actores")
public class Actor {

    @Id // ESTA ANOTACIÓN DEFINE EL ATRIBUTO COMO LA CLAVE PRIMARIA ÚNICA DE LA TABLA PARA QUE JPA PUEDA IDENTIFICAR CADA REGISTRO DE FORMA INDIVIDUAL
    @GeneratedValue(strategy = GenerationType.IDENTITY) // ESTABLECE QUE EL VALOR DEL IDENTIFICADOR ES AUTOINCREMENTAL Y QUE LA RESPONSABILIDAD DE GENERARLO RECAE EN EL TIPO "SERIAL" DE POSTGRESQL
    @Column(name = "idActor") // ASOCIA EXPLÍCITAMENTE ESTE ATRIBUTO DE JAVA CON EL NOMBRE EXACTO DE LA COLUMNA DEFINIDA EN EL SCRIPT DE CREACIÓN DE LA BASE DE DATOS
    private Integer idActor;

    private String nome;
    private String apelidos;
    private String nacionalidade;

    @ManyToOne(fetch = FetchType.LAZY) // ESTABLECE UNA RELACIÓN DE "MUCHOS A UNO" INDICANDO QUE MÚLTIPLES ACTORES PUEDEN ESTAR VINCULADOS A UNA ÚNICA PELÍCULA, CARGANDO LOS DATOS SOLO CUANDO SEAN NECESARIOS PARA AHORRAR MEMORIA
    @JoinColumn(name = "id_pelicula", referencedColumnName = "idPelicula") // DEFINE LA COLUMNA QUE ACTÚA COMO CLAVE FORÁNEA EN LA TABLA ACTORES Y ESPECIFICA QUE SE CONECTA CON LA COLUMNA IDPELICULA DE LA TABLA PELICULAS
    @JsonIgnore // ESTA ANOTACIÓN ES CRUCIAL PARA EL MICROSERVICIO PORQUE EVITA LA RECURSIÓN INFINITA AL TRANSFORMAR EL OBJETO A JSON, IMPIDIENDO QUE EL ACTOR INTENTE SERIALIZAR SU PELÍCULA Y ESTA A SU VEZ A SUS ACTORES
    private Pelicula pelicula; // ATRIBUTO QUE REPRESENTA EL OBJETO PADRE AL QUE PERTENECE ESTE REGISTRO, PERMITIENDO NAVEGAR HACIA LA INFORMACIÓN DE LA PELÍCULA DESDE EL ACTOR

    public Actor() {
        // CONSTRUCTOR VACÍO OBLIGATORIO POR LA ESPECIFICACIÓN DE JPA PARA PODER CREAR LAS INSTANCIAS DE LA CLASE DE FORMA DINÁMICA MEDIANTE REFLECTION DURANTE LA RECUPERACIÓN DE DATOS
    }

    public Integer getIdActor() { return idActor; }
    public void setIdActor(Integer idActor) { this.idActor = idActor; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getApelidos() { return apelidos; }
    public void setApelidos(String apelidos) { this.apelidos = apelidos; }

    public String getNacionalidade() { return nacionalidade; }
    public void setNacionalidade(String nacionalidade) { this.nacionalidade = nacionalidade; }

    public Pelicula getPelicula() { return pelicula; }
    public void setPelicula(Pelicula pelicula) { this.pelicula = pelicula; }
}
```

```java
package org.example.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "peliculas")
public class Pelicula {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // INDICA QUE EL ID ES AUTOINCREMENTAL Y QUE LA BASE DE DATOS SE ENCARGA DE GENERARLO
    @Column(name = "idPelicula") // MAPEADO EXPLÍCITO PARA ASEGURAR QUE COINCIDA CON EL NOMBRE DE LA COLUMNA EN EL SCRIPT SQL
    private Integer idPelicula;

    @Column(name = "titulo") // VINCULA ESTA VARIABLE CON LA COLUMNA TITULO DE LA TABLA
    private String titulo;

    @Column(name = "xenero")
    private String xenero;

    @Column(name = "ano")
    private Integer ano;

    @OneToMany(mappedBy = "pelicula", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    // DEFINE LA RELACIÓN DE UNA PELÍCULA HACIA MUCHOS ACTORES USANDO EL CAMPO PELICULA DE LA CLASE ACTOR
    // CASCADE ALL PERMITE QUE SI BORRAMOS UNA PELÍCULA TAMBIÉN SE BORREN SUS ACTORES ASOCIADOS
    // FETCH LAZY EVITA CARGAR LOS ACTORES DE GOLPE A MENOS QUE LOS PIDAMOS EXPLÍCITAMENTE, MEJORANDO EL RENDIMIENTO
    private List<Actor> actores; // LISTA QUE CONTENDRÁ TODOS LOS OBJETOS ACTOR VINCULADOS A ESTA PELÍCULA

    public Pelicula() {} // CONSTRUCTOR POR DEFECTO REQUERIDO POR JPA PARA PODER CREAR INSTANCIAS DINÁMICAMENTE

    public Integer getIdPelicula() {
        return idPelicula;
    }

    public void setIdPelicula(Integer idPelicula) {
        this.idPelicula = idPelicula;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getXenero() {
        return xenero;
    }

    public void setXenero(String xenero) {
        this.xenero = xenero;
    }

    public Integer getAno() {
        return ano;
    }

    public void setAno(Integer ano) {
        this.ano = ano;
    }

    public List<Actor> getActores() {
        return actores;
    }

    public void setActores(List<Actor> actores) {
        this.actores = actores;
    }

}
```





