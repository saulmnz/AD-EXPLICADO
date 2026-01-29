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