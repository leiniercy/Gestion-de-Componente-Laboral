/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.application.data.service;

import com.example.application.data.entity.Profesor;
import com.example.application.data.repository.ProfesorRepository;
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
public class ProfesorService {

    private ProfesorRepository repository;

    public ProfesorService(@Autowired ProfesorRepository repository) {
        this.repository = repository;
    }

    public Optional<Profesor> get(Integer id) {
        return repository.findById(id);
    }

    public Profesor update(Profesor entity) {
        return repository.save(entity);
    }

    public void deletebyId(Integer id) {
        repository.deleteById(id);
    }

    public void delete(Profesor p) {
        repository.delete(p);
    }

    public Page<Profesor> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public int count() {
        return (int) repository.count();
    }
}

