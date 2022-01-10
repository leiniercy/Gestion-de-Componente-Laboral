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
            + "where dtype like 'Profesor' "
            + "and (lower(p.nombre) like lower(concat('%', :searchTerm, '%')) "
            + " or lower(p.apellidos) like lower(concat('%', :searchTerm, '%'))"
            + " or lower(p.email) like lower(concat('%', :searchTerm, '%'))"
            + " or lower(p.solapin) like lower(concat('%', :searchTerm, '%'))"
            + " or lower(p.jefe_area) like lower(concat('%', :searchTerm, '%'))"
            + " or lower(a.nombre) like lower(concat('%', :searchTerm, '%')) )"
    )
    List<Profesor> searchProfesor(@Param("searchTerm") String searchTerm);

    //filtar Profesor por nombre
    @Query("select p from Profesor p "
            + "join Area a on p.id  = a.id "
            + "where dtype like 'Profesor' "
            + "and lower(p.nombre) like lower(concat('%', :searchTerm, '%')) "
    )
    List<Profesor> searchProfesorNombre(@Param("searchTerm") String searchTerm);

    //filtar Profesor por apellidos
    @Query("select p from Profesor p "
            + "join Area a on p.id  = a.id "
            + "where dtype like 'Profesor' "
            + "and lower(p.apellidos) like lower(concat('%', :searchTerm, '%')) "
    )
    List<Profesor> searchProfesorApellidos(@Param("searchTerm") String searchTerm);

    //filtar Profesor por email
    @Query("select p from Profesor p "
            + "join Area a on p.id  = a.id "
            + "where dtype like 'Profesor' "
            + "and lower(p.email) like lower(concat('%', :searchTerm, '%')) "
    )
    List<Profesor> searchProfesorEmail(@Param("searchTerm") String searchTerm);

    //filtar Profesor por solapin
    @Query("select p from Profesor p "
            + "join Area a on p.id  = a.id "
            + "where dtype like 'Profesor' "
            + "and lower(p.solapin) like lower(concat('%', :searchTerm, '%')) "
    )
    List<Profesor> searchProfesorSolpain(@Param("searchTerm") String searchTerm);

    //filtar Profesor por Jefe de Area
    @Query("select p from Profesor p "
            + "join Area a on p.id  = a.id "
            + "where dtype like 'Profesor' "
            + "and lower(p.jefe_area) like lower(concat('%', :searchTerm, '%')) "
    )
    List<Profesor> searchProfesorJefe_area(@Param("searchTerm") String searchTerm);
    
    //filtar Profesor Area
    @Query("select p from Profesor p "
            + "join Area a on p.id  = a.id "
            + "where dtype like 'Profesor' "
            + "and lower(a.nombre) like lower(concat('%', :searchTerm, '%')) "
    )
    List<Profesor> searchProfesorArea(@Param("searchTerm") String searchTerm);
    
    
    
}
