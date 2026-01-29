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