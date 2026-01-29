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