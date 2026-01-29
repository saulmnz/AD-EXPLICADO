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