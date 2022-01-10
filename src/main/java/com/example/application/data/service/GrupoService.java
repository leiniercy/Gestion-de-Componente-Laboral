package com.example.application.data.service;

import com.example.application.data.entity.Grupo;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.vaadin.crudui.crud.CrudListener;
import com.example.application.data.repository.GrupoRepository;


@Service
@RequiredArgsConstructor
public class GrupoService {

    private final GrupoRepository repository;


    public Optional<Grupo> get(Integer id) {
        return repository.findById(id);
    }
    
    
    public Grupo update(Grupo entity) {
        return repository.save(entity);
    }

    public void deleteById (Integer id) {
        repository.deleteById(id);
    }

    public Page<Grupo> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public int count() {
        return (int) repository.count();
    }

 
}
