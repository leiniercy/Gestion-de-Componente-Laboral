/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.application.data.entity;

import com.example.application.data.AbstractEntity;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Email;
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
@Table(name = "Estudiantes")
public class Estudiante extends AbstractEntity {

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
    

//    @NotBlank(message = "campo vacio")
//    @Column(name = "anno_repitencia")
//    @Size(message = "número no valido" , max = 1, min=1)
        private Integer anno_repitencia;

//    @NotBlank(message = "campo vacio")
//    @Column(name = "cantidad_asignaturas")
//    @Size(message = "número no valido" , max = 1, min=1)
    private Integer cantidad_asignaturas;

//    @NotNull(message = "debe elegir un campo")
    @JoinColumn(name = "area_id")
    @ManyToOne
    private Area area;

//    @NotNull(message = "debe elegir un campo")
    @JoinColumn(name = "grupo_id")
    @ManyToOne
    private Grupo grupo;

    @OneToMany(mappedBy = "estudiante")
    private List<Evaluacion> evaluaciones;

    @OneToMany(mappedBy = "e")
    private List<Tarea> tareas;

    public String getStringNombreApellidos() {
        return getNombre() + " " + getApellidos();
    }

}
