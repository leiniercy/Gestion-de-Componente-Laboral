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
public class GrupoService {

    private final GrupoRepository repository;

    public GrupoService(@Autowired GrupoRepository repository) {
        this.repository = repository;
    }

    public List<Grupo> findAllGrupo() {
        return repository.findAll();
    }

    public List<Grupo> searchGrupo(Integer searchTerm) {
        return repository.search(searchTerm);
    }

    public Grupo findById(Integer id) {
        return repository.findById(id).get();
    }

    public long countGrupo() {
        return repository.count();
    }

    public void deleteGrupo(Grupo grupo) {
        repository.delete(grupo);
    }

    public void saveGrupo(Grupo grupo) {
        if (grupo == null) {
            System.err.println("This field is null. Are you sure you have connected your form to the application?");
            return;
        }
        repository.save(grupo);
    }

}
