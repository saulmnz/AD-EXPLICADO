package org.example.repository;

import org.example.model.Peliculas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

// REPOSITORIO JPA PARA LA ENTIDAD PELICULAS
@Repository
public interface PeliculasRepository extends JpaRepository<Peliculas, Long> {
    // METODOS DE BUSQUEDA PERSONALIZADOS SEGUN REQUISITOS
    List<Peliculas> findByTitulo(String titulo);
    List<Peliculas> findByXenero(String xenero);
}