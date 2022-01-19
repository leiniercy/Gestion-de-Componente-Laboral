/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.application.views.Prueba;

import javax.persistence.Entity;

import com.example.application.data.AbstractEntity;

/**
 *
 * @author Leinier
 */
@Entity
public class Status extends AbstractEntity {

    private String name;

    public Status() {
    }

    public Status(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
