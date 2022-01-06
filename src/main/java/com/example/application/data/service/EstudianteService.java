/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.application.data.service;

import com.example.application.data.entity.Area;
import com.example.application.data.entity.Estudiante;
import com.example.application.data.repository.AreaRepository;
import com.example.application.data.repository.EstudianteRepository;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.vaadin.crudui.crud.CrudListener;

/**
 *
 * @author Leinier
 */
@Service
public class EstudianteService {

    private EstudianteRepository estudianteRepository;

    public EstudianteService(
            @Autowired EstudianteRepository estudianteRepository
           ) {
        this.estudianteRepository = estudianteRepository;
    }

    public Optional<Estudiante> get(Integer id) {
        return estudianteRepository.findById(id);
    }

    public Estudiante update(Estudiante entity) {
        return estudianteRepository.save(entity);
    }

    public void deleteById(Integer id) {
        estudianteRepository.deleteById(id);
    }
    
    public  void delete (Estudiante estudiante){
        estudianteRepository.delete(estudiante);
    }

    public Page<Estudiante> list(Pageable pageable) {
        return estudianteRepository.findAll(pageable);
    }

    public long count() {
        return  estudianteRepository.count();
    }
}
