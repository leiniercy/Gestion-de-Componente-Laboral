/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.application.data.entity;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
/**
 *
 * @author Leinier
 */

@AllArgsConstructor
@NoArgsConstructor

public enum Rol {

    USER("user"), ADMIN("admin");

    private String rolname;

    public String getRolname() {
        return rolname;
    }

    public void setRolname(String rolname) {
        this.rolname = rolname;
    }
    
}
