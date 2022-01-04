/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.application.data.entity;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
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
@Table(name = "Estudiantes")
public class Estudiante extends Person{
   
    @EqualsAndHashCode.Include
    @ToString.Include
    @Column(name = "anno_repitencia")
    private Integer anno_repitencia;
    @Column(name = "cantidad_asignaturas")
    private Integer cantidad_asignaturas;
    
    @ManyToOne
    private Area area;
    @OneToMany(mappedBy = "estudiante")
    private List<Evaluacion> evaluaciones;
    @OneToMany(mappedBy = "e")
    private List<Tarea> tareas;
}
