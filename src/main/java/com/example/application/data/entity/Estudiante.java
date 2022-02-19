/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.application.data.entity;

import com.example.application.data.AbstractEntity;

import java.util.List;
import javax.persistence.*;
import javax.validation.constraints.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


/**
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
@Table(name = "Estudiantes")
public class Estudiante extends AbstractEntity {

    @EqualsAndHashCode.Include
    @ToString.Include

    @NotEmpty
    @NotBlank(message = "El campo no debe estar vacío")
    @Pattern(regexp = "^[a-zA-ZÀ-ÿ\\u00f1\\u00d1]+(\\s*[a-zA-ZÀ-ÿ\\u00f1\\u00d1]*)*[a-zA-ZÀ-ÿ\\u00f1\\u00d1]+$" ,message = "Solo letras") //0 combinaciones de letras 0 o mas veces incluyendo espacios
    @Size(message = "Mínimo 2 caracteres y máximo 100",min=2,max = 100)
    @Column(name = "nombre", nullable = false)
    private String nombre;

    @NotEmpty
    @NotBlank(message = "El campo no debe estar vacío")
    @Pattern(regexp = "^[a-zA-ZÀ-ÿ\\u00f1\\u00d1]+(\\s*[a-zA-ZÀ-ÿ\\u00f1\\u00d1]*)*[a-zA-ZÀ-ÿ\\u00f1\\u00d1]+$" ,message = "Solo letras") //0 combinaciones de letras 0 o mas veces incluyendo espacios
    @Size(message = "Mínimo 3 caracteres y máximo 100",min=3,max = 100)
    @Column(name = "apellidos" ,nullable = false)
    private String apellidos;


    @NotNull(message = "campo vacío")
    @OneToOne()
    private User user;

    @Email
    @NotEmpty
    @NotBlank(message = "El campo no debe estar vacío")
    @Pattern(regexp = "^[a-zA-Z][a-zA-Z0-9_\\.][a-zA-Z0-9]+(@estudiantes\\.uci\\.cu)$" , message = "Por favor escriba un correo válido" )
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @NotEmpty
    @NotBlank(message = "El campo no debe estar vacío")
    @Pattern(regexp = "^[A-Z][0-9]+$" ,message = "Solo letras y numeros")
    @Size(message = "Mínimo 7 caracteres y máximo 7 ", min=7, max = 7)
    @Column(name = "solapin", nullable = false, unique = true)
    private String solapin;


    @NotNull(message = "campo vacío")
    @Column(name = "anno_repitencia")
    @Max(message = "Máximo 5", value = 5)
    @Min(message = "Mínimo 1", value = 1)
    private Integer anno_repitencia;

    @NotNull(message = "campo vacío")
    @Column(name = "cantidad_asignaturas")
    @Max(message = "Máximo 16", value = 16)
    @Min(message = "Mínimo 2", value = 2)
    private Integer cantidad_asignaturas;

    @NotNull(message = "debe elegir un campo")
    @JoinColumn(name = "area_id")
    @ManyToOne
    private Area area;

    @NotNull(message = "debe elegir un campo")
    @JoinColumn(name = "grupo_id")
    @ManyToOne
    private Grupo grupo;

    @OneToMany(mappedBy = "estudiante")
    @ElementCollection(fetch = FetchType.EAGER)
    private List<Evaluacion> evaluaciones;

    @OneToMany(mappedBy = "e")
    @ElementCollection(fetch = FetchType.EAGER)
    private List<Tarea> tareas;


    public String getStringNombreApellidos() {
        return getNombre() + " " + getApellidos();
    }

}
