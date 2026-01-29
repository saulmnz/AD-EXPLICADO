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