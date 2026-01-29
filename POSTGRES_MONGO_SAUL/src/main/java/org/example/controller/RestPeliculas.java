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