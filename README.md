# AD 

![IMG](https://i.pinimg.com/originals/46/4f/d9/464fd9d9ba7c3450c9a8ae98f3e03362.gif)

---

## PASOS INICIALES 🐲

>[!TIP]
> 1. ***Levantar la máquina virtual, asegurar la correcta conexión y accesibilidad con la VM, postgres y mongoDB***
> 2. ***Lanzar el script de creación de tablas para postgreSQL***
> 3. ***Configuración del application.properties***
> 4. ***Una vez hecho lo anterior y configurado todos los archivos -> ejecutar PelisPostgres***
> 5. ***Esperar 10 segundos y ejecutar MongoChamador***

---

# CONFIGURACIÓN DE PELISPOSTGRES 🦦

> [!NOTE]
> ***Empezamos primero a configurar PelisPostgres***

---

### ESTRUCTURA DE ARCHIVOS

![img](ESTRUCTURA/123.png)


### ESTRUCTURA SQL ⛓️‍💥

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

### APPLICATION.PROPERTIES 🥣

> [!CAUTION]
> ***Este archivo define las propiedades necesarias para que el microservicio sepa donde conectarse y qué puerto escuchar.***

```java
spring.datasource.url=jdbc:postgresql://172.20.10.2:5432/postgres 
spring.datasource.username=postgres 
spring.datasource.password=postgres 
spring.datasource.driver-class-name=org.postgresql.Driver 
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect 
spring.jpa.hibernate.ddl-auto=update 
server.port=8085 
```

---


### MODEL/ENTIDADES Peliculas.java Y Actores.java 🎨🎨

- **Al definir una clase como @Entity, le decimos a Spring que cree una tabla basada en esa clase y, con @Table( name = "actores") aseguras que en postgres sea exactamente el nomnbre que indicamos. Hay que diferenciar una cosa, en Java, en este microservicio por ejemplo, Actor tiene un objeto de tipo Película dentro `private Peliculas pelicula;` , en SQL, Actor solo tiene el ID de la película en la columna correspodiente ( la columna de la clave foránea id_pelicula ).**

> [!CAUTION]
> ***En JPA ( API de Java ) estas clases representan las tablas de la bas de datos, los nombres de los campos deben coincidir con las columnas que definen el script SQL para que el mapeo ( la traducción de java a SQL ) sea automática. En este microservicio usamos JPA ( Jakarta Persistence API ). Su función se resume al mapeo relacional, le dice al programa que la clase actor es la tabla actores, además de definir la clave foránea de esa clase.***

> [!NOTE]
> ***La relación @ManyToOne en el archivo Actor.java de este microservicio define que muchos actores pertenecen a una pelicula. en el código se define como @ManyToOne(fetch = FetchType.EAGER), además, tendremos que indicarle la columna que será la clave foránea de la en la tabla, @JoinColumn(name = "id_pelicula"). @JsonManagedReference (Padre) y @JsonBackReference (Hijo): CRÍTICO. Evitan que al convertir a JSON el programa entre en un bucle infinito (Película llama a Actor -> Actor llama a Película -> Película llama a Actor...).***


```java
package org.example.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

// CLASE ENTIDAD QUE MAPEA LA TABLA 'ACTORES' EN POSTGRES
@Entity
@Table(name = "actores")
public class Actores {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idActor;

    private String nome;
    private String apelidos;
    private String nacionalidade;

    // RELACION MUCHOS A UNO
    // JSONBACKREFERENCE EVITA BUCLES INFINITOS AL SERIALIZAR JSON
    @ManyToOne
    @JoinColumn(name = "id_pelicula")
    @JsonBackReference
    private Peliculas pelicula;

    public Actores() {}

    public Actores(String nome, String apelidos, String nacionalidade, Peliculas pelicula) {
        this.nome = nome;
        this.apelidos = apelidos;
        this.nacionalidade = nacionalidade;
        this.pelicula = pelicula;
    }

    // GETTERS Y SETTERS
    public Long getIdActor() { return idActor; }
    public void setIdActor(Long idActor) { this.idActor = idActor; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getApelidos() { return apelidos; }
    public void setApelidos(String apelidos) { this.apelidos = apelidos; }
    public String getNacionalidade() { return nacionalidade; }
    public void setNacionalidade(String nacionalidade) { this.nacionalidade = nacionalidade; }
    public Peliculas getPelicula() { return pelicula; }
    public void setPelicula(Peliculas pelicula) { this.pelicula = pelicula; }
}
```

```java
package org.example.model;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

// CLASE ENTIDAD QUE MAPEA LA TABLA 'PELICULAS' EN POSTGRES
@Entity
@Table(name = "peliculas")
public class Peliculas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPelicula;

    @Column(length = 150)
    private String titulo;

    @Column(length = 50)
    private String xenero;

    private Integer ano;

    // RELACION UNO A MUCHOS BIDIRECCIONAL CON ACTORES
    // JSONMANAGEDREFERENCE PERMITE SERIALIZAR LA LISTA DE HIJOS
    @OneToMany(mappedBy = "pelicula", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Actores> actores = new ArrayList<>();

    public Peliculas() {}

    public Peliculas(String titulo, String xenero, Integer ano) {
        this.titulo = titulo;
        this.xenero = xenero;
        this.ano = ano;
    }

    // GETTERS Y SETTERS
    public Long getIdPelicula() { return idPelicula; }
    public void setIdPelicula(Long idPelicula) { this.idPelicula = idPelicula; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getXenero() { return xenero; }
    public void setXenero(String xenero) { this.xenero = xenero; }
    public Integer getAno() { return ano; }
    public void setAno(Integer ano) { this.ano = ano; }
    public List<Actores> getActores() { return actores; }

    // METODO PARA MANTENER LA CONSISTENCIA DE LA RELACION
    public void setActores(List<Actores> actores) {
        this.actores = actores;
        if(this.actores != null){
            this.actores.forEach(a -> a.setPelicula(this));
        }
    }
}
```

---

### REPOSITORY PeliculaRepository.java Y ActorRepository.java 🧁

>[!NOTE]
> **El repository actúa como mediador entre el código de java y la base de datos postgreSQL. La función que tiene es permitir hacer las funciones CRUD, sin tener que escribir líneas de código SQL. Ambos ficheros heredan JpaRepository.**


> [!CAUTION]
> `***JpaRepository<Pelicula, Integer>`, de esta manera le indicamos a Spring que ese repositorio maneja la entidad Pelicula y que su clave primaria ( el @id ) es de tipo Integer. Al heredar de esta manera lo que hacemos es ahorrar código, Spring escribirá de forma automática por debajo los métodos save(), findAll(), findById, deleteById.***

- **`List<Pelicula> findByTitulo(String titulo);` : Una de las cosas a resaltar de esta capa ( Repository ) es que existe la posibilidad de crear búsquedas simplemente nombrando bien los métodos, con `findBy` Spring sabe que quieres realizar un select, con Titulo sabe que debe buscar en la columna titulo de la tabla.****


```java
package org.example.repository;

import org.example.model.Actores;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

// REPOSITORIO JPA PARA LA ENTIDAD ACTORES
@Repository
public interface ActoresRepository extends JpaRepository<Actores, Long> {
    // METODOS DE BUSQUEDA PERSONALIZADOS SEGUN REQUISITOS
    List<Actores> findByNome(String nome);
    List<Actores> findByNacionalidade(String nacionalidade);
}
}

```

```java
package org.example.repository;

import org.example.model.Peliculas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

// REPOSITORIO JPA PARA LA ENTIDAD PELICULAS
@Repository
public interface PeliculasRepository extends JpaRepository<Peliculas, Long> {
    // METODOS DE BUSQUEDA PERSONALIZADOS SEGUN REQUISITOS
    List<Peliculas> findByTitulo(String titulo);
    List<Peliculas> findByXenero(String xenero);
}

```

---

### SERVICE PeliculaService.java Y ActorService.java 🦂

>[!NOTE]
>***La capa Service es el intermediario obligatorio entre el Controlador (quien recibe las órdenes) y el Repositorio (quien tiene los datos).***

- ***Gestión de Nulos: El uso de Optional<Peliculas>. Definimos que puede contener una película o estar vacía. Esto nos indica que el dato podría no existir, evitando errores NullPointerException en tiempo de ejecución.***

```java
package org.example.service;

import org.example.model.Peliculas;
import org.example.repository.PeliculasRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

// SERVICIO DE NEGOCIO PARA PELICULAS
@Service
public class PeliculasService {

    @Autowired
    private PeliculasRepository peliculasRepository;

    // GUARDAR PELICULA
    public Peliculas save(Peliculas pelicula) {
        return peliculasRepository.save(pelicula);
    }

    // BUSCAR POR ID RETORNANDO OPTIONAL 
    public Optional<Peliculas> findById(Long id) {
        return peliculasRepository.findById(id);
    }

    // BUSCAR POR TITULO
    public List<Peliculas> findByTitulo(String titulo) {
        return peliculasRepository.findByTitulo(titulo);
    }

    // LISTAR TODAS
    public List<Peliculas> findAll() {
        return peliculasRepository.findAll();
    }

    // BORRAR POR ID
    public void deleteById(Long id) {
        peliculasRepository.deleteById(id);
    }
}
```

```java
package org.example.service;

import org.example.model.Actores;
import org.example.repository.ActoresRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

// SERVICIO DE NEGOCIO PARA ACTORES
@Service
public class ActoresService {

    @Autowired
    private ActoresRepository actoresRepository;

    // GUARDAR ACTOR
    public Actores save(Actores actor) {
        return actoresRepository.save(actor);
    }

    // BUSCAR POR ID RETORNANDO OPTIONAL
    public Optional<Actores> findById(Long id) {
        return actoresRepository.findById(id);
    }

    // BUSCAR POR NOMBRE
    public List<Actores> findByNome(String nome) {
        return actoresRepository.findByNome(nome);
    }

    // LISTAR TODOS
    public List<Actores> findAll() {
        return actoresRepository.findAll();
    }

    // BORRAR POR ID
    public void deleteById(Long id) {
        actoresRepository.deleteById(id);
    }
}
```

---

### CONTROLLER RestPeliculas.java Y RestActores.java🦠

>[!NOTE]
>***El Controller es la "puerta de entrada". Se encarga de escuchar las peticiones HTTP que llegan por la red al puerto 8085. Su función principal no es pensar, sino recibir la orden, pasársela al Service y devolver una respuesta adecuada al cliente (en este caso, al microservicio MongoChamador).***

>[!CAUTION]
>***`ResponseEntity<T>:` No devolvemos el objeto "a secas" (un return Pelicula). Lo envolvemos en un ResponseEntity. ¿Por qué? Porque así tenemos control total sobre el Código de Estado HTTP. Si todo va bien, devolvemos un 200 OK (ResponseEntity.ok()), pero si buscamos un ID que no existe, podemos devolver un error 404 (ResponseEntity.notFound()). Esto es vital para que quien consuma nuestra API sepa qué ha pasado.***

>***`@RestController` : A diferencia de un @Controller normal (que devolvería una página web HTML), esta anotación indica que la respuesta será puro texto en formato JSON, que es el estándar para la comunicación entre microservicios.***


```java
package org.example.controller;

import org.example.model.Peliculas;
import org.example.service.PeliculasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

// CONTROLADOR REST PARA PELICULAS
@RestController
@RequestMapping("/postgres/peliculas")
public class RestPeliculas {

    @Autowired
    private PeliculasService peliculasService;

    // CREAR PELICULA
    @PostMapping
    public ResponseEntity<Peliculas> create(@RequestBody Peliculas pelicula) {
        // VINCULAMOS ACTORES A LA PELICULA ANTES DE GUARDAR
        if(pelicula.getActores() != null) {
            pelicula.getActores().forEach(a -> a.setPelicula(pelicula));
        }
        return ResponseEntity.ok(peliculasService.save(pelicula));
    }

    // OBTENER POR ID USANDO MAP Y ORELSEGET (OPTIONAL)
    @GetMapping("/{id}")
    public ResponseEntity<Peliculas> getById(@PathVariable Long id) {
        return peliculasService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // OBTENER POR TITULO
    @GetMapping("/titulo/{titulo}")
    public ResponseEntity<List<Peliculas>> getByTitulo(@PathVariable String titulo) {
        List<Peliculas> peliculas = peliculasService.findByTitulo(titulo);
        if(peliculas.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(peliculas);
    }

    // LISTAR TODAS
    @GetMapping
    public ResponseEntity<List<Peliculas>> getAll() {
        return ResponseEntity.ok(peliculasService.findAll());
    }
}
```

```java
package org.example.controller;

import org.example.model.Actores;
import org.example.service.ActoresService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

// CONTROLADOR REST PARA ACTORES
@RestController
@RequestMapping("/postgres/actores")
public class RestActores {

    @Autowired
    private ActoresService actoresService;

    // CREAR ACTOR
    @PostMapping
    public ResponseEntity<Actores> create(@RequestBody Actores actor) {
        return ResponseEntity.ok(actoresService.save(actor));
    }

    // OBTENER POR ID
    @GetMapping("/{id}")
    public ResponseEntity<Actores> getById(@PathVariable Long id) {
        return actoresService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // OBTENER POR NOMBRE
    @GetMapping("/nome/{nome}")
    public ResponseEntity<List<Actores>> getByNome(@PathVariable String nome) {
        List<Actores> actores = actoresService.findByNome(nome);
        return ResponseEntity.ok(actores);
    }

    // LISTAR TODOS
    @GetMapping
    public ResponseEntity<List<Actores>> getAll() {
        return ResponseEntity.ok(actoresService.findAll());
    }
}
```

---

# CONFIGURACIÓN DE MONGOCHAMADOR 

>[!NOTE]
>***En Postgres teníamos dos tablas separadas, pero en Mongo optamos por la desnormalización para ganar velocidad de lectura.***


### ESTRUCTURA DE ARCHIVOS

![img](ESTRUCTURA/456.png)

---

###  APPLICATION.PROPERTIES 👹

```java
spring.data.mongodb.uri=mongodb://172.20.10.2:27017/peliculasDB
spring.data.mongodb.database=peliculasDB
server.port=8086
```

---

### MODEL Peliculas.java Y Actores.java 💀

>[!CAUTION]
> ***@Document(collection = "peliculas"): Le indicamos a Mongo que esto se guardará en la colección "peliculas". Lo más importante aquí es la lista List<Actores>. Al no usar JPA, no hay relaciones complejas; simplemente, el objeto Película guarda dentro de sí mismo (embebida) la lista de todos sus actores. Si borras la película, se borran sus actores, porque son parte del documento.***

```java
package org.example.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

// MODELO DE DOCUMENTO MONGODB
@Document(collection = "peliculas")
public class Peliculas {

    @Id
    private String id; // ID PROPIO DE MONGODB (String, autogenerado)

    // CAMBIO IMPORTANTE: USAMOS EL MISMO NOMBRE QUE EN EL JSON DE POSTGRES
    private Long idPelicula;

    private String titulo;
    private String xenero;
    private Integer ano;

    // LISTA DE ACTORES EMBEBIDA
    private List<Actores> actores;

    public Peliculas() {}

    // GETTERS Y SETTERS
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    // AHORA ESTE GETTER SI EXISTE Y COINCIDE CON TU LLAMADA EN SECUENCIA
    public Long getIdPelicula() { return idPelicula; }
    public void setIdPelicula(Long idPelicula) { this.idPelicula = idPelicula; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getXenero() { return xenero; }
    public void setXenero(String xenero) { this.xenero = xenero; }
    public Integer getAno() { return ano; }
    public void setAno(Integer ano) { this.ano = ano; }
    public List<Actores> getActores() { return actores; }
    public void setActores(List<Actores> actores) { this.actores = actores; }
}
```

```java
package org.example.model;

// CLASE SIMPLE PARA LOS ACTORES DENTRO DE MONGO
// NO ES UN @DOCUMENT, ES PARTE DE LA PELICULA
public class Actores {

    private Long idActor; // ID QUE VIENE DE POSTGRES
    private String nome;
    private String apelidos;
    private String nacionalidade;

    public Actores() {}

    public Actores(String nome, String apelidos, String nacionalidade) {
        this.nome = nome;
        this.apelidos = apelidos;
        this.nacionalidade = nacionalidade;
    }

    // GETTERS Y SETTERS
    public Long getIdActor() { return idActor; }
    public void setIdActor(Long idActor) { this.idActor = idActor; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getApelidos() { return apelidos; }
    public void setApelidos(String apelidos) { this.apelidos = apelidos; }
    public String getNacionalidade() { return nacionalidade; }
    public void setNacionalidade(String nacionalidade) { this.nacionalidade = nacionalidade; }
}
```

---

### REPOSITORY PeliculasRepository ActoresRepository 🥷

```java
package org.example.repository;

import org.example.model.Peliculas;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PeliculasRepository extends MongoRepository<Peliculas, String> {
}

```

```java
package org.example.repository;

import org.example.model.Actores;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActoresRepository extends MongoRepository<Actores, Long> {
    // SE USA LONG AQUI PORQUE EN TU ACTORESSERVICE BUSCAS POR LONG
    // AUNQUE MONGO USA STRING POR DEFECTO, SPRING DATA INTENTARA ADAPTARLO
}
```

---

### SERVICE ConexionService.java ActoresService.java PeliculasService.java Secuencia.java 👻

>[!TIP]
> ***`ConexionService.java` (El Cliente HTTP) Aquí usamos RestTemplate. Este servicio actúa como un navegador web: hace peticiones GET y POST a la URL http://localhost:8085/postgres/peliculas. Su trabajo es traerse el JSON que genera el otro microservicio y convertirlo (deserializarlo) en objetos Java que podamos usar.***

>[!TIP]
> ***`PeliculasService.java` Se encarga de hablar con PeliculasRepository para guardar los datos en Mongo. El Truco del ID nulo: Antes de guardar, hacemos pelicula.setId(null). ¿Por qué? Porque el objeto viene con un ID numérico de Postgres. Si intentamos guardar eso en el campo _id de Mongo, daría error o sobrescribiría documentos. Al ponerlo a null, forzamos a Mongo a crear un documento nuevo con un ID hash único.***

```java
package org.example.service;

import org.example.model.Peliculas;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

@Service
public class ConexionService {

    @Autowired
    private RestTemplate restTemplate;

    // CONSTANTES CON LAS URLS DEL OTRO MICROSERVICIO (PUERTO 8085)
    private static final String URL_BASE = "http://localhost:8085/postgres/peliculas";

    // OBTENER UNA PELICULA POR ID (GET)
    public Peliculas getPeliculasById(Long id) {
        try {
            String url = URL_BASE + "/" + id;
            ResponseEntity<Peliculas> response = restTemplate.exchange(
                    url, HttpMethod.GET, null, Peliculas.class
            );
            return response.getBody();
        } catch (HttpClientErrorException e) {
            System.out.println("ERROR CONECTANDO A POSTGRES (ID " + id + "): " + e.getMessage());
            return null;
        }
    }

    // OBTENER PELICULAS POR TITULO (GET) - DEVUELVE LISTA
    public List<Peliculas> getPeliculasByTitulo(String titulo) {
        try {
            String url = URL_BASE + "/titulo/" + titulo;
            ResponseEntity<List<Peliculas>> response = restTemplate.exchange(
                    url, HttpMethod.GET, null,
                    new ParameterizedTypeReference<List<Peliculas>>() {}
            );
            return response.getBody();
        } catch (HttpClientErrorException e) {
            System.out.println("ERROR CONECTANDO A POSTGRES (TITULO " + titulo + "): " + e.getMessage());
            return Collections.emptyList();
        }
    }

    // CREAR UNA PELICULA NUEVA EN POSTGRES (POST)
    public Peliculas createPeliculas(Peliculas pelicula) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Peliculas> request = new HttpEntity<>(pelicula, headers);

            ResponseEntity<Peliculas> response = restTemplate.exchange(
                    URL_BASE, HttpMethod.POST, request, Peliculas.class
            );
            return response.getBody();
        } catch (HttpClientErrorException e) {
            System.out.println("ERROR AL CREAR EN POSTGRES: " + e.getMessage());
            return null;
        }
    }
}
```

---

### Secuencia.java 🐨

```java
package org.example.service;

import org.example.model.Actores;
import org.example.model.Peliculas;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class Secuencia {

    private final ConexionService conexionService;
    private final PeliculasService peliculasService;

    // INYECCION DE DEPENDENCIAS
    public Secuencia(ConexionService conexionService, PeliculasService peliculasService) {
        this.conexionService = conexionService;
        this.peliculasService = peliculasService;
    }

    public void executar() {
        System.out.println("INICIANDO SECUENCIAAAAAAAAAAAAAAAAAAAA");

        // LIMPIAR
        peliculasService.borrarTodo();


        // PREPARAR DATOS DE PRUEBA EN MEMORIA
        List<Actores> Titanic = new ArrayList<>();
        Titanic.add(new Actores("Leonardo", "DiCaprio", "Otaku"));
        Titanic.add(new Actores("Otaku", "Otake", "Otaku"));
        Titanic.add(new Actores("Bobi", "Otakech", "Otaku"));
        Peliculas p1 = new Peliculas();
        p1.setTitulo("TITANIC");
        p1.setXenero("TERROR");
        p1.setAno(1997);
        p1.setActores(Titanic);

        List<Actores> Avatar = new ArrayList<>();
        Avatar.add(new Actores("Mari", "MJ", "ONICHa"));
        Avatar.add(new Actores("Arigato", "Brawl", "Stars"));
        Avatar.add(new Actores("Dirham", "Juli", "Royale"));
        Peliculas p2 = new Peliculas();
        p2.setTitulo("Avatar");
        p2.setXenero("Sci-Fi");
        p2.setAno(2009);
        p2.setActores(Avatar);


        // INSERTAR EN POSTGRES
        System.out.println("\nENVIANDO DATOSSSS");

        // AL GUARDAR, RECUPERAMOS EL OBJETO CON EL ID QUE LE HA PUESTO LA BBDD
        p1 = conexionService.createPeliculas(p1);
        p2 = conexionService.createPeliculas(p2);


        // SINCRONIZAR POR ID (LEER DE POSTGRES -> GUARDAR EN MONGO)
        if (p1 != null) {
            System.out.println("\nBUSCANDO POR ID: " + p1.getIdPelicula());

            // PEDIMOS A POSTGRES QUE NOS DEVUELVA LA PELICULA COMPLETA
            Peliculas recuperadaId = conexionService.getPeliculasById(p1.getIdPelicula());

            if (recuperadaId != null) {
                // LA GUARDAMOS EN MONGO
                peliculasService.guardarEnMongo(recuperadaId);
            }
        }

        // SINCRONIZAR POR TITULO (LEER DE POSTGRES -> GUARDAR EN MONGO)
        String tituloBuscar = "Avatar";
        System.out.println("\nBUSCANDO POR TITULO: " + tituloBuscar);

        List<Peliculas> resultados = conexionService.getPeliculasByTitulo(tituloBuscar);

        if (resultados != null && !resultados.isEmpty()) {
            Peliculas recuperadaTitulo = resultados.get(0); // COGEMOS LA PRIMERA

            // LA GUARDAMOS EN MONGO
            peliculasService.guardarEnMongo(recuperadaTitulo);
        }

        // EXPORTAR RESULTADO A JSON
        System.out.println("\nGENERANDO ARCHIVO JSON...");
        peliculasService.exportarJson();

        System.out.println("\nSECUENCIA FINALISSSSSADA");
    }
}
```

```java
package org.example.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.example.model.Peliculas;
import org.example.repository.PeliculasRepository;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

@Service
public class PeliculasService {

    private final PeliculasRepository peliculasRepo;

    // INYECCION POR CONSTRUCTOR
    public PeliculasService(PeliculasRepository peliculasRepo) {
        this.peliculasRepo = peliculasRepo;
    }

    // METODO PARA GUARDAR EN MONGO ( CREAR O ACTUALIZAR LA PELÍCULA )
    public void guardarEnMongo(Peliculas pelicula) {
        // AL TRAER EL OBJETO DE POSTGRES, VIENE CON UN ID NUMERICO.
        // MONGO NECESITA SU PROPIO ID (STRING HASH). SI NO PONEMOS EL ID A NULL,
        // SPRING DATA INTENTARA USAR EL NUMERO COMO _ID Y PUEDE DAR ERROR.
        pelicula.setId(null);

        // GUARDAMOS LA PELICULA (CON SUS ACTORES DENTRO)
        peliculasRepo.save(pelicula);
        System.out.println("PELICULA GUARDADA EN MONGOOOUODB: " + pelicula.getTitulo());
    }

    // METODO PARA EXPORTAR A JSON
    public void exportarJson() {

        // RECUPERAMOS TODOS LOS DOCUMENTOS DE LA COLECCION DE MONGO
        List<Peliculas> lista = peliculasRepo.findAll();

        // CONFIGURAMOS GSON CON 'PRETTY PRINTING' PARA QUE EL ARCHIVO SEA LEGIBLE
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String nombreArchivo = "peliculas_mongo.json";

        try (FileWriter escritor = new FileWriter(nombreArchivo)) {
            gson.toJson(lista, escritor);
            System.out.println("JSON GENERADO EN '" + nombreArchivo + "'");
        } catch (IOException e) {
            System.err.println("ERROR AL ESCRIBIR EL JSON: " + e.getMessage());
        }
    }

    // LIMPIAR BD
    public void borrarTodo() {
        peliculasRepo.deleteAll();
    }
}
```

```java
package org.example.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.example.model.Actores;
import org.example.repository.ActoresRepository;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.List;

@Service
public class ActoresService {

    private final ActoresRepository ActoresRepo;

    public ActoresService(ActoresRepository ActoresRepo) {
        this.ActoresRepo = ActoresRepo;
    }

    public void crearActualizarActores(Actores a) {
        ActoresRepo.save(a);
    }

    public void borrarActoress() {
        ActoresRepo.deleteAll();
    }

    public Actores buscarActores(Long id) {
        return ActoresRepo.findById(id).orElse(null);
    }

    public List<Actores> buscarActoreses() {
        return ActoresRepo.findAll();
    }

}
```

---

### CONFIG config.java  MongoConfig.java🦛

```java
package org.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class Config {

    // BEAN NECESARIO PARA COMUNICARSE CON EL MICROSERVICIO PELISPOSTGRES
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
```

```java
package org.example.config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MongoConfig {

    @Value("${spring.data.mongodb.uri}")
    private String mongoUri;

    @Bean
    public MongoClient mongoClient() {
        return MongoClients.create(mongoUri);
    }
}
```

---

### MAIN Main.java 🦭

>[!NOTE]
>***`@PostConstruct` : Esta anotación es vital. Le dice a Spring: "Oye, en cuanto termines de cargar toda la configuración y de conectarte a las bases de datos, ejecuta inmediatamente el método executar()". Sin esto, el programa arrancaría y se quedaría esperando sin hacer nada. `System.exit(200)` : Una vez que la secuencia termina, matamos el proceso con código 200 (Todo OK). Esto libera la consola y la memoria.***

```java
package org.example;
import jakarta.annotation.PostConstruct;
import org.example.service.Secuencia;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Main {

    private final Secuencia secuencia;

    // INYECCION DE DEPENDENCIAS POR CONSTRUCTOR
    public Main(Secuencia secuencia) {
        this.secuencia = secuencia;
    }

    // SE EJECUTA AUTOMATICAMENTE AL LEVANTAR SPRING
    @PostConstruct
    public void executar() {
        secuencia.executar();
        System.out.println("FINALIZANDO APLICACION CON EXIT CODE 200");
        System.exit(200);
    }
    
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}
```

---

### SALIDA EN JSON 🦜

```JSON
[
  {
    "id": "697b9452d2e7161e1573712a",
    "idPelicula": 1,
    "titulo": "TITANIC",
    "xenero": "TERROR",
    "ano": 1997,
    "actores": [
      {
        "idActor": 1,
        "nome": "Leonardo",
        "apelidos": "DiCaprio",
        "nacionalidade": "Otaku"
      },
      {
        "idActor": 2,
        "nome": "Otaku",
        "apelidos": "Otake",
        "nacionalidade": "Otaku"
      },
      {
        "idActor": 3,
        "nome": "Bobi",
        "apelidos": "Otakech",
        "nacionalidade": "Otaku"
      }
    ]
  },
  {
    "id": "697b9452d2e7161e1573712b",
    "idPelicula": 2,
    "titulo": "Avatar",
    "xenero": "Sci-Fi",
    "ano": 2009,
    "actores": [
      {
        "idActor": 4,
        "nome": "Mari",
        "apelidos": "MJ",
        "nacionalidade": "ONICHa"
      },
      {
        "idActor": 5,
        "nome": "Arigato",
        "apelidos": "Brawl",
        "nacionalidade": "Stars"
      },
      {
        "idActor": 6,
        "nome": "Dirham",
        "apelidos": "Juli",
        "nacionalidade": "Royale"
      }
    ]
  }
]
```

