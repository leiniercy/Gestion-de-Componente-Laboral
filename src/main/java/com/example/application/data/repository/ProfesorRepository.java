/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.application.data.repository;

import com.example.application.data.entity.Profesor;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Leinier
 */
@Repository
public interface ProfesorRepository extends JpaRepository<Profesor,Integer> {
    
    
    //filtar Profesor
    
    @Query("select p from Profesor p "
            + "join Area a on p.id  = a.id "
            + "where lower(p.nombre) like lower(concat('%', :searchTerm, '%')) "
            + " or lower(p.apellidos) like lower(concat('%', :searchTerm, '%'))"
            + " or lower(p.email) like lower(concat('%', :searchTerm, '%'))"
            + " or lower(p.solapin) like lower(concat('%', :searchTerm, '%'))"
            + " or lower(a.nombre) like lower(concat('%', :searchTerm, '%'))"
    )
    List<Profesor> searchProfesor(@Param("searchTerm") String searchTerm);
   
}

