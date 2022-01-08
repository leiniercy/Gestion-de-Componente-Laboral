/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.application.data.repository;

import com.example.application.data.entity.Area;
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
public interface AreaRepository extends JpaRepository<Area, Integer> {

    @Query("SELECT a FROM Area a "
            + "WHERE lower(a.nombre) like lower(concat('%', :searchTerm, '%')) "
            +" or lower(a.descripcion) like lower(concat('%', :searchTerm, '%')) "
    )
    List<Area> search(@Param("searchTerm") String searchTerm);

}
