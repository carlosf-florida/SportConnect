package com.example.api.Repository;

import com.example.api.Model.Clase;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ClaseRepository extends JpaRepository<Clase, Integer> {
    List<Clase> findByProfesorId(int profesorId);
}
