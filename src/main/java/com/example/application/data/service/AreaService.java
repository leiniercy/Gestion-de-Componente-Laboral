/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.application.data.service;

import com.example.application.data.entity.Area;
import com.example.application.data.repository.AreaRepository;
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
public class AreaService {

    private AreaRepository repository;

    public AreaService(@Autowired AreaRepository repository) {
        this.repository = repository;
    }

    public List<Area> findAllArea() {
        return repository.findAll();
    }

    public List<Area> searchArea(String stringFilter) {
        return repository.search(stringFilter);
    }

    public Area findById(Integer id) {
        return repository.findById(id).get();
    }

    public long countArea() {
        return repository.count();
    }

    public void deleteArea(Area area) {
        repository.delete(area);
    }

    public void saveArea(Area area) {
        if (area == null) {
            System.err.println("This field is null. Are you sure you have connected your form to the application?");
            return;
        }

        if (area.getId() == null) {
            repository.save(area);
            Notification.show("Área añadida");
        } else {
            repository.save(area);
            Notification.show("Área modificada");
        }

    }

}
