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
import com.vaadin.flow.component.notification.Notification;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
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
            @Autowired EstudianteRepository estudianteRepository) {
        this.estudianteRepository = estudianteRepository;
    }

    public List<Estudiante> findAllEstudiante() {
        return estudianteRepository.findAll();
    }

    public Estudiante findById(Integer id) {
        return estudianteRepository.findById(id).get();
    }

    public List<Estudiante> searchEstudiante(String searchTerm) {
        return estudianteRepository.searchEstudiante(searchTerm);
    }

    public long countEstudiante() {
        return estudianteRepository.count();
    }

    public void deleteEstudiante(Estudiante estudiante) {
        estudianteRepository.delete(estudiante);
    }

    public void saveEstudiante(Estudiante estudiante) {
        if (estudiante == null) {
            System.err.println("This field is null. Are you sure you have connected your form to the application?");
            return;
        }

        if (estudiante.getId() == null) {
            estudianteRepository.save(estudiante);
             Notification.show("Estudiante añadido");

        } else {
            estudianteRepository.save(estudiante);
             Notification.show("Estudiante modificado");
        }

    }
}
