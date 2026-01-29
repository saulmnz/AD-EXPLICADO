package org.example.repository;

import org.example.model.Peliculas;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PeliculasRepository extends MongoRepository<Peliculas, String> {
}