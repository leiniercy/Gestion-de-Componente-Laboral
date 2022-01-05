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
    
     @Query("select p from Person p " +
        "where lower(p.nombre) like lower(person('%', :searchTerm, '%')) " +
        "or lower(p.apellidos) like lower(person('%', :searchTerm, '%'))")
    List<Person> search(@Param("searchTerm") String searchTerm);   
    
}
