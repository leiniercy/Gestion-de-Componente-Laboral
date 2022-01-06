/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.application.data.service;

import com.example.application.data.entity.Evaluacion;
import com.example.application.data.repository.EvaluacionRepository;
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

    public Optional<Evaluacion> get(Integer id) {
        return repository.findById(id);
    }

    public Evaluacion update(Evaluacion entity) {
        return repository.save(entity);
    }

    public void deleteById(Integer id) {
        repository.deleteById(id);
    }
    
    public void delete(Evaluacion evaluacion) {
        repository.delete(evaluacion);
    }

    public Page<Evaluacion> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public int count() {
        return (int) repository.count();
    }

}
