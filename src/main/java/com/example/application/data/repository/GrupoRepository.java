package com.example.application.data.repository;

import com.example.application.data.entity.Grupo;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface GrupoRepository extends JpaRepository<Grupo, Integer> {
     
    @Query("SELECT g FROM Grupo g "
            + "WHERE  g.numero = :searchTerm  "
    )
    List<Grupo> search(@Param("searchTerm") Integer searchTerm);
}