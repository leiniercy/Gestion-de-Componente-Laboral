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
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
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
    
//    @NotEmpty
//    @NotBlank(message = "campo vacío")
//    @Column(name = "nombre" ,length = 255, nullable = false)
    private String nombre;
    
//    @NotEmpty
//    @NotBlank(message = "campo vacío")
//    @Column(name = "descripcion",length = 255, nullable = false)
    private String descripcion;
    
//   @NotNull(message = "campo vacío")
//    @Column(name = "fecha_inicio")
    private LocalDate fecha_inicio;

//    @NotNull(message = "campo vacío")
//    @Column(name = "fecha_fin")
    private LocalDate fecha_fin;
    
//    @NotNull(message = "debe elegir un campo")
    @ManyToOne
    private Estudiante e;
    
    @OneToMany(mappedBy = "tarea")
    private List<Evaluacion> evaluaciones; 
    
}
