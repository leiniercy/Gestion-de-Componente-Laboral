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

    //filtar Estudiante
    @Query("select e from Estudiante e "
            + "join Area a on e.id  = a.id "
            + "join Grupo g on e.id  = g.id "
            + "where dtype like 'Estudiante' "
            + "and (lower(e.nombre) like lower(concat('%', :searchTerm, '%')) "
            + " or lower(e.apellidos) like lower(concat('%', :searchTerm, '%'))"
            + " or lower(e.email) like lower(concat('%', :searchTerm, '%'))"
            + " or lower(e.solapin) like lower(concat('%', :searchTerm, '%'))"
            + " or lower(e.anno_repitencia) like lower(concat('%', :searchTerm, '%'))"
            + " or lower(e.cantidad_asignaturas) like lower(concat('%', :searchTerm, '%'))"
            + " or lower(a.nombre) like lower(concat('%', :searchTerm, '%'))"
            + " or lower(g.numero) like lower(concat('%', :searchTerm, '%'))  )"
    )
    List<Estudiante> searchEstudiante(@Param("searchTerm") String searchTerm);

    //filtar Estudiante por nombre
    @Query("select e from Estudiante e "
            + "join Area a on e.id  = a.id "
            + "join Grupo g on e.id  = g.id "
            + "where dtype like 'Estudiante' "
            + "and lower(e.nombre) like lower(concat('%', :searchTerm, '%')) "
    )
    List<Estudiante> searchEstudianteNombre(@Param("searchTerm") String searchTerm);

    //filtar Estudiante por apellidos
    @Query("select e from Estudiante e "
            + "join Area a on e.id  = a.id "
            + "join Grupo g on e.id  = g.id "
            + "where dtype like 'Estudiante' "
            + "and lower(e.apellidos) like lower(concat('%', :searchTerm, '%')) "
    )
    List<Estudiante> searchEstudianteApellidos(@Param("searchTerm") String searchTerm);

    //filtar Estudiante por email
    @Query("select e from Estudiante e "
            + "join Area a on e.id  = a.id "
            + "join Grupo g on e.id  = g.id "
            + "where dtype like 'Estudiante' "
            + "and lower(e.email) like lower(concat('%', :searchTerm, '%')) "
    )
    List<Estudiante> searchEstudianteEmail(@Param("searchTerm") String searchTerm);

    //filtar Estudiante por solpain
    @Query("select e from Estudiante e "
            + "join Area a on e.id  = a.id "
            + "join Grupo g on e.id  = g.id "
            + "where dtype like 'Estudiante' "
            + "and lower(e.solapin) like lower(concat('%', :searchTerm, '%')) "
    )
    List<Estudiante> searchEstudianteSolapin(@Param("searchTerm") String searchTerm);

    //filtar Estudiante por año de repitencia
    @Query("select e from Estudiante e "
            + "join Area a on e.id  = a.id "
            + "join Grupo g on e.id  = g.id "
            + "where dtype like 'Estudiante' "
            + "and lower(e.anno_repitencia) like lower(concat('%', :searchTerm, '%')) "
    )
    List<Estudiante> searchEstudianteAnno_repitencia(@Param("searchTerm") String searchTerm);

    //filtar Estudiante por cantidad de asignaturas
    @Query("select e from Estudiante e "
            + "join Area a on e.id  = a.id "
            + "join Grupo g on e.id  = g.id "
            + "where dtype like 'Estudiante' "
            + "and lower(e.cantidad_asignaturas) like lower(concat('%', :searchTerm, '%')) "
    )
    List<Estudiante> searchEstudianteCantidad_asignaturas(@Param("searchTerm") String searchTerm);

    //filtar Estudiante por area
    @Query("select e from Estudiante e "
            + "join Area a on e.id  = a.id "
            + "join Grupo g on e.id  = g.id "
            + "where dtype like 'Estudiante' "
            + "and lower(a.nombre) like lower(concat('%', :searchTerm, '%')) "
    )
    List<Estudiante> searchEstudianteArea(@Param("searchTerm") String searchTerm);

    //filtar Estudiante por grupo
    @Query("select e from Estudiante e "
            + "join Area a on e.id  = a.id "
            + "join Grupo g on e.id  = g.id "
            + "where dtype like 'Estudiante' "
            + "and lower(g.numero) like lower(concat('%', :searchTerm, '%')) "
    )
    List<Estudiante> searchEstudianteGrupo(@Param("searchTerm") String searchTerm);
}
