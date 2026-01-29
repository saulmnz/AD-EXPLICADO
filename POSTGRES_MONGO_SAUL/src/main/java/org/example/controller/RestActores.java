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