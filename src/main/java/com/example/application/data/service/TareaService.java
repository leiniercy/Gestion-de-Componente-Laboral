
package com.example.application.data.service;

import com.example.application.data.entity.Tarea;
import com.example.application.data.repository.TareaRepository;
import java.time.LocalDate;
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
public class TareaService {

    private TareaRepository repository;

    public TareaService(@Autowired TareaRepository repository) {
        this.repository = repository;
    }

    public List<Tarea> findAllTareas() {
        return repository.findAll();
    }

    public List<Tarea> searchTarea(String searchTerm) {
        return repository.search(searchTerm);
    }
    public List<Tarea> searchTareaByFecha(LocalDate searchTerm) {
        return repository.searchFecha(searchTerm);
    }

    public long countTarea() {
        return repository.count();
    }

    public void deleteTarea(Tarea tarea) {
        repository.delete(tarea);
    }

    public void saveTarea(Tarea tarea) {
        if (tarea == null) {
            System.err.println("This field is null. Are you sure you have connected your form to the application?");
            return;
        }
        repository.save(tarea);
    }

}
