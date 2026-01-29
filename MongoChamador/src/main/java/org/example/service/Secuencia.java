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