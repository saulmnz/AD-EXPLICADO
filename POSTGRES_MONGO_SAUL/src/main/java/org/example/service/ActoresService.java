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