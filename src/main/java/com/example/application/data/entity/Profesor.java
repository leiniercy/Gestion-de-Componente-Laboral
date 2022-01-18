/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.application.data.entity;

import com.example.application.data.AbstractEntity;
import javax.persistence.ManyToOne;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 *
 * @author Leinier
 */
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "Profesor")
public class Profesor extends AbstractEntity {

    @EqualsAndHashCode.Include
    @ToString.Include
//    
//    @NotEmpty
//    @NotBlank(message = "campo vacío")
//    @Column(name = "nombre", nullable = false)
    private String nombre;

//    @NotEmpty
//    @NotBlank(message = "campo vacío")
//    @Column(name = "apellidos" , nullable = false)
    private String apellidos;

//    @Email
//    @NotEmpty
//    @NotBlank(message = "campo vacío")
//    @Column(name = "email" , nullable = false)
    private String email;

//    @NotEmpty
//    @NotBlank(message = "campo vacío")
//    @Column(name = "solapin", nullable = false, unique = true)
    private String solapin;

    @NotNull(message = "debe elegir un campo")
    @JoinColumn(name = "a_id")
    @ManyToOne
    private Area a;

    @OneToOne()
    private User user;

    public String getStringNombreApellidos() {
        return getNombre() + " " + getApellidos();
    }

}
