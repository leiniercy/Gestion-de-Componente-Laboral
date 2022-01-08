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
public interface EstudianteRepository extends JpaRepository<Estudiante, Integer> {

//    @Query(
//              "select p from persons p "
//            + "join areas a on p.id  = a.id "
//            + "join grupo g on p.id  = g.id "
//            + "where dtype like 'Estudiante' "
//            + "and lower(p.nombre)"
//            + "like lower(concat('%' ,:searchTerm, '%'))"
//        )
//    List<Estudiante> searchByName(@Param("searchTerm") String searchTerm);

}
