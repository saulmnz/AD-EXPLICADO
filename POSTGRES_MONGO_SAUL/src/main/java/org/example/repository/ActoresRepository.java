package org.example.repository;

import org.example.model.Actores;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

// REPOSITORIO JPA PARA LA ENTIDAD ACTORES
@Repository
public interface ActoresRepository extends JpaRepository<Actores, Long> {
    // METODOS DE BUSQUEDA PERSONALIZADOS SEGUN REQUISITOS
    List<Actores> findByNome(String nome);
    List<Actores> findByNacionalidade(String nacionalidade);
}