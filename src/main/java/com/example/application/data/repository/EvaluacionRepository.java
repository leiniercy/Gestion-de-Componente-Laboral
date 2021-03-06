/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.application.data.repository;

import com.example.application.data.entity.Evaluacion;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Leinier
 */
@Repository
public interface EvaluacionRepository extends JpaRepository<Evaluacion, Integer> {

    //filtrar
    @Query("SELECT e FROM Evaluacion e JOIN Estudiante p on e.id = p.id "
            + "WHERE  lower(e.nota) like lower(concat('%', :searchTerm , '%')) "
            + " or lower(e.descripcion) like lower(concat('%', :searchTerm, '%')) "
            + " or lower(p.nombre) like lower(concat('%', :searchTerm , '%'))"
            + " or lower(p.apellidos) like lower(concat('%', :searchTerm , '%'))"
    )
    List<Evaluacion> search(@Param("searchTerm") String searchTerm);

    
    
    
}
