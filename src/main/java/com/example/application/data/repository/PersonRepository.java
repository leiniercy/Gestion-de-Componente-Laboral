/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.application.data.repository;

import com.example.application.data.entity.Person;
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
public interface PersonRepository extends JpaRepository<Person, Integer> {

    //filtar Estudiante
    @Query("select p from Person p "
            + "join Area a on p.id  = a.id "
            + "join Grupo g on p.id  = g.id "
            + "where dtype like 'Estudiante' "
            + "and (lower(p.nombre) like lower(concat('%', :searchTerm, '%')) "
            + " or lower(p.apellidos) like lower(concat('%', :searchTerm, '%'))"
            + " or lower(p.email) like lower(concat('%', :searchTerm, '%'))"
            + " or lower(p.solapin) like lower(concat('%', :searchTerm, '%'))"
            + " or lower(p.anno_repitencia) like lower(concat('%', :searchTerm, '%'))"
            + " or lower(p.cantidad_asignaturas) like lower(concat('%', :searchTerm, '%'))"
            + " or lower(a.nombre) like lower(concat('%', :searchTerm, '%'))"
            + " or lower(g.numero) like lower(concat('%', :searchTerm, '%'))  )"
    )
    List<Person> searchEstudiante(@Param("searchTerm") String searchTerm);

    //filtar Estudiante por nombre
    @Query("select p from Person p "
            + "join Area a on p.id  = a.id "
            + "join Grupo g on p.id  = g.id "
            + "where dtype like 'Estudiante' "
            + "and lower(p.nombre) like lower(concat('%', :searchTerm, '%')) "
    )
    List<Person> searchEstudianteNombre(@Param("searchTerm") String searchTerm);

    //filtar Estudiante por apellidos
    @Query("select p from Person p "
            + "join Area a on p.id  = a.id "
            + "join Grupo g on p.id  = g.id "
            + "where dtype like 'Estudiante' "
            + "and lower(p.apellidos) like lower(concat('%', :searchTerm, '%')) "
    )
    List<Person> searchEstudianteApellidos(@Param("searchTerm") String searchTerm);

    //filtar Estudiante por email
    @Query("select p from Person p "
            + "join Area a on p.id  = a.id "
            + "join Grupo g on p.id  = g.id "
            + "where dtype like 'Estudiante' "
            + "and lower(p.email) like lower(concat('%', :searchTerm, '%')) "
    )
    List<Person> searchEstudianteEmail(@Param("searchTerm") String searchTerm);

    //filtar Estudiante por solpain
    @Query("select p from Person p "
            + "join Area a on p.id  = a.id "
            + "join Grupo g on p.id  = g.id "
            + "where dtype like 'Estudiante' "
            + "and lower(p.solapin) like lower(concat('%', :searchTerm, '%')) "
    )
    List<Person> searchEstudianteSolapin(@Param("searchTerm") String searchTerm);

    //filtar Estudiante por año de repitencia
    @Query("select p from Person p "
            + "join Area a on p.id  = a.id "
            + "join Grupo g on p.id  = g.id "
            + "where dtype like 'Estudiante' "
            + "and lower(p.anno_repitencia) like lower(concat('%', :searchTerm, '%')) "
    )
    List<Person> searchEstudianteAnno_repitencia(@Param("searchTerm") String searchTerm);

    //filtar Estudiante por cantidad de asignaturas
    @Query("select p from Person p "
            + "join Area a on p.id  = a.id "
            + "join Grupo g on p.id  = g.id "
            + "where dtype like 'Estudiante' "
            + "and lower(p.cantidad_asignaturas) like lower(concat('%', :searchTerm, '%')) "
    )
    List<Person> searchEstudianteCantidad_asignaturas_repitencia(@Param("searchTerm") String searchTerm);

    //filtar Estudiante por area
    @Query("select p from Person p "
            + "join Area a on p.id  = a.id "
            + "join Grupo g on p.id  = g.id "
            + "where dtype like 'Estudiante' "
            + "and lower(a.nombre) like lower(concat('%', :searchTerm, '%')) "
    )
    List<Person> searchEstudianteArea(@Param("searchTerm") String searchTerm);

    //filtar Estudiante por grupo
    @Query("select p from Person p "
            + "join Area a on p.id  = a.id "
            + "join Grupo g on p.id  = g.id "
            + "where dtype like 'Estudiante' "
            + "and lower(g.numero) like lower(concat('%', :searchTerm, '%')) "
    )
    List<Person> searchEstudianteGrupo(@Param("searchTerm") String searchTerm);

    //filtar Profesor
    @Query("select p from Person p "
            + "join Area a on p.id  = a.id "
            + "where dtype like 'Profesor' "
            + "and (lower(p.nombre) like lower(concat('%', :searchTerm, '%')) "
            + " or lower(p.apellidos) like lower(concat('%', :searchTerm, '%'))"
            + " or lower(p.email) like lower(concat('%', :searchTerm, '%'))"
            + " or lower(p.solapin) like lower(concat('%', :searchTerm, '%'))"
            + " or lower(p.jefe_area) like lower(concat('%', :searchTerm, '%'))"
            + " or lower(a.nombre) like lower(concat('%', :searchTerm, '%')) )"
    )
    List<Person> searchProfesor(@Param("searchTerm") String searchTerm);

    //filtar Profesor por nombre
    @Query("select p from Person p "
            + "join Area a on p.id  = a.id "
            + "where dtype like 'Profesor' "
            + "and lower(p.nombre) like lower(concat('%', :searchTerm, '%')) "
    )
    List<Person> searchProfesorNombre(@Param("searchTerm") String searchTerm);

    //filtar Profesor por apellidos
    @Query("select p from Person p "
            + "join Area a on p.id  = a.id "
            + "where dtype like 'Profesor' "
            + "and lower(p.apellidos) like lower(concat('%', :searchTerm, '%')) "
    )
    List<Person> searchProfesorApellidos(@Param("searchTerm") String searchTerm);

    //filtar Profesor por email
    @Query("select p from Person p "
            + "join Area a on p.id  = a.id "
            + "where dtype like 'Profesor' "
            + "and lower(p.email) like lower(concat('%', :searchTerm, '%')) "
    )
    List<Person> searchProfesorEmail(@Param("searchTerm") String searchTerm);

    //filtar Profesor por solapin
    @Query("select p from Person p "
            + "join Area a on p.id  = a.id "
            + "where dtype like 'Profesor' "
            + "and lower(p.solapin) like lower(concat('%', :searchTerm, '%')) "
    )
    List<Person> searchProfesorSolpain(@Param("searchTerm") String searchTerm);

    //filtar Profesor por Jefe de Area
    @Query("select p from Person p "
            + "join Area a on p.id  = a.id "
            + "where dtype like 'Profesor' "
            + "and lower(p.jefe_area) like lower(concat('%', :searchTerm, '%')) "
    )
    List<Person> searchProfesorJefe_area(@Param("searchTerm") String searchTerm);
    
    //filtar Profesor Area
    @Query("select p from Person p "
            + "join Area a on p.id  = a.id "
            + "where dtype like 'Profesor' "
            + "and lower(a.nombre) like lower(concat('%', :searchTerm, '%')) "
    )
    List<Person> searchProfesorArea(@Param("searchTerm") String searchTerm);
    
    

}
