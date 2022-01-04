/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.application.data.service;

import com.example.application.data.entity.Person;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.example.application.data.repository.PersonRepository;
import java.util.Collection;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.vaadin.crudui.crud.CrudListener;

/**
 *
 * @author Leinier
 */
@Service
public class PersonService  {

    private  PersonRepository repository;

    public PersonService(@Autowired PersonRepository repository) {
        this.repository = repository;
    }

    public Optional<Person> get(Integer id) {
        return repository.findById(id);
    }

    public Person update(Person entity) {
        return repository.save(entity);
    }

    public void delete(Integer id) {
        repository.deleteById(id);
    }

    public Page<Person> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public int count() {
        return (int) repository.count();
    }
    
}
