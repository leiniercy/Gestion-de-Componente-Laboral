/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.application.data.service;

import com.example.application.data.entity.User;
import com.example.application.data.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 *
 * @author Leinier
 */
@Service
public class UserService {
    
     private UserRepository repository;

    public UserService(@Autowired UserRepository repository) {
        this.repository = repository;
    }

     public List<User> findAllUser() {
        return repository.findAll();
    }
     
    public User findById(Long id){
       return repository.findById(id).get();
    }
    

    public long countUser() {
        return repository.count();
    }

    public void deleteUser(User user) {
       repository.delete(user);
    }

    public void saveUser(User user) {
        if (user == null) {
            System.err.println("This field is null. Are you sure you have connected your form to the application?");
            return;
        }
       repository.save(user);
    }

    public long count() {
        return repository.count();
    }
}
