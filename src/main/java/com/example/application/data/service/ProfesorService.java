/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.application.data.service;

import com.example.application.data.entity.Profesor;
import com.example.application.data.entity.User;
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

    public List<Profesor> findAllProfesor() {
        return repository.findAll();
    }

    public List<Profesor> searchProfesor(String searchTerm) {
        return repository.searchProfesor(searchTerm);
    }

    public Profesor findById(Integer id) {
        return repository.findById(id).get();
    }
  
    public long countProfesor() {
        return repository.count();
    }

    public void deleteProfesor(Profesor profesor) {
        repository.delete(profesor);
    }

    public void saveProfesor(Profesor profesor) {
        if (profesor == null) {
            System.err.println("This field is null. Are you sure you have connected your form to the application?");
            return;
        }
        repository.save(profesor);
    }

}
