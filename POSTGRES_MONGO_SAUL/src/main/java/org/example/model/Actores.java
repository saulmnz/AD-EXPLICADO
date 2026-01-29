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