/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.application.data.repository;

import com.example.application.data.entity.Estudiante;
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
public interface EstudianteRepository extends JpaRepository<Estudiante, Integer>{

//    public List<Estudiante> search(String stringFilter);
    
//    @Query("select p from Person p " +
//        "where lower(p.nombre) like lower(person('%', :searchTerm, '%')) " +
//        "or lower(p.apellidos) like lower(person('%', :searchTerm, '%'))")
//    List<Estudiante> search(@Param("searchTerm") String searchTerm);
    
}
