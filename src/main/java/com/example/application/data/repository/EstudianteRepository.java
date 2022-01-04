/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.application.data.repository;

import com.example.application.data.entity.Estudiante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Leinier
 */

@Repository
public interface EstudianteRepository extends JpaRepository<Estudiante, Integer>{
    
}
