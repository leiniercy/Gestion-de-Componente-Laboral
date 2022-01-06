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
public class GrupoService implements CrudListener<Grupo>{

    private final GrupoRepository repository;


    public Optional<Grupo> get(Integer id) {
        return repository.findById(id);
    }
    
    @Override
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

    @Override
    public List<Grupo> findAll() {
        return repository.findAll();
    }

    @Override
    public Grupo add(Grupo arg0) {
        return repository.save(arg0);
    }

    @Override
    public void delete(Grupo arg0) {
        repository.delete(arg0);
    }

}
