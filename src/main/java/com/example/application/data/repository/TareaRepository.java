/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.application.data.repository;

import com.example.application.data.entity.Tarea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author Leinier
 */
@Repository
public interface TareaRepository extends JpaRepository<Tarea, Integer> {

    //filtrar
    @Query("SELECT t from Tarea t JOIN Estudiante e on t.id = e.id "
            + " WHERE lower(t.nombre) like lower(concat('%', :searchTerm, '%')) "
            + "or lower(t.descripcion) like lower(concat('%', :searchTerm, '%')) "
            + "or lower(e.nombre) like lower(concat('%', :searchTerm, '%')) "
            + "or lower(e.apellidos) like lower(concat('%', :searchTerm, '%')) "
            + " or t.fecha_fin = :searchTerm "
    )
    List<Tarea> search(@Param("searchTerm") String searchTerm);

    //filtrar pot fecha
    @Query("SELECT t from Tarea t JOIN Estudiante e on t.id = e.id "
            + " WHERE t.fecha_inicio = :searchTerm "
            + " or t.fecha_fin = :searchTerm "
    )
    List<Tarea> searchFecha(@Param("searchTerm") LocalDate searchTerm);
    


}
