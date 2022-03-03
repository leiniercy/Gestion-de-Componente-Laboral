/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.application.data.service;

import com.example.application.data.entity.Evaluacion;
import com.example.application.data.repository.EvaluacionRepository;
import com.vaadin.flow.component.notification.Notification;
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
public class EvaluacionService {

    private EvaluacionRepository repository;

    public EvaluacionService(@Autowired EvaluacionRepository repository) {
        this.repository = repository;
    }

    public List<Evaluacion> findAllEvaluacion() {
        return repository.findAll();
    }

    public List<Evaluacion> searchEvaluacion(String searchTerm) {
        return repository.search(searchTerm);
    }

    public Evaluacion findById(Integer id) {
        return repository.findById(id).get();
    }

    public long countEvaluacion() {
        return repository.count();
    }

    public void deleteEvaluacion(Evaluacion evaluacion) {
        repository.delete(evaluacion);
    }

    public void saveEvaluacion(Evaluacion evaluacion) {
        if (evaluacion == null) {
            System.err.println("This field is null. Are you sure you have connected your form to the application?");
            return;
        }
        if (evaluacion.getId() == null) {
            repository.save(evaluacion);
            Notification.show("Evaluación añadida");
        } else {
            repository.save(evaluacion);
            Notification.show("Evaluación modificada");
        }
        
    }

}
