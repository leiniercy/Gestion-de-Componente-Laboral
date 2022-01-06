/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.application.data.service;

import com.example.application.data.entity.Area;
import com.example.application.data.repository.AreaRepository;
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
public class AreaService {
   
    private AreaRepository repository;

   public AreaService(@Autowired AreaRepository repository) {
        this.repository = repository;
    }

    public Optional<Area> get(Integer id) {
        return repository.findById(id);
    }

    public Area update(Area entity) {
        return repository.save(entity);
    }

    public void deleteByID(Integer id) {
        repository.deleteById(id);
    }
    
    public void delete(Area area) {
        repository.delete(area);
    }

    public Page<Area> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public int count() {
        return (int) repository.count();
    }
}
