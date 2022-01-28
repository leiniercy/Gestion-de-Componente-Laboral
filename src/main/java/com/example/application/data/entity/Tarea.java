/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.application.data.entity;

import com.example.application.data.AbstractEntity;
import com.vaadin.flow.component.charts.model.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
@Table(name = "Tareas")
public class Tarea extends AbstractEntity{

    @EqualsAndHashCode.Include
    @ToString.Include

    @NotEmpty
    @NotBlank(message = "El campo no debe estar vacío")
    @Pattern(regexp = "^[a-zA-Z][a-zA-Z\\s]+$" ,message = "Solo letras") //0 combinaciones de letras 0 o mas veces incluyendo espacios
    @Size(message = "Mínimo 2 caracteres y máximo 50",min=2,max = 50)
    @Column(name = "nombre",nullable = false)
    private String nombre;

    @NotEmpty
    @NotBlank(message = "El campo no debe estar vacío")
    @Pattern(regexp = "^[a-zA-Z][a-zA-Z0-9\\s]*$", message = "Solo letras y numeros")
    @Size(message = "Mínimo 3 caracteres, máximo 255",min = 3,max =255)
    @Column(name = "descripcion", nullable = true)
    private String descripcion;
    
    @NotNull(message = "campo vacío")
    @Column(name = "fecha_inicio")
    private LocalDate fecha_inicio;

    @NotNull(message = "campo vacío")
    @Column(name = "fecha_fin")
    private LocalDate fecha_fin;
    
    @NotNull(message = "debe elegir un campo")
    @ManyToOne
    private Estudiante e;
    
    @OneToMany(mappedBy = "tarea")
    @ElementCollection(fetch = FetchType.EAGER)
    private List<Evaluacion> evaluaciones; 
    
}
