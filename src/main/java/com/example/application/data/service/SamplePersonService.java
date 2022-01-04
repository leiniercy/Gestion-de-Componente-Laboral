package com.example.application.data.service;

import com.example.application.data.entity.SamplePerson;
import com.example.application.data.repository.SamplePersonRepository;
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


@Service
@RequiredArgsConstructor
public class SamplePersonService implements CrudListener<SamplePerson>{

    private final SamplePersonRepository repository;


    public Optional<SamplePerson> get(Integer id) {
        return repository.findById(id);
    }
    
    @Override
    public SamplePerson update(SamplePerson entity) {
        return repository.save(entity);
    }

    public void deleteById (Integer id) {
        repository.deleteById(id);
    }

    public Page<SamplePerson> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public int count() {
        return (int) repository.count();
    }

    @Override
    public List<SamplePerson> findAll() {
        return repository.findAll();
    }

    @Override
    public SamplePerson add(SamplePerson arg0) {
        return repository.save(arg0);
    }

    @Override
    public void delete(SamplePerson arg0) {
        repository.delete(arg0);
    }

}
