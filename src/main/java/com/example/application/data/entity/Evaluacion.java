/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.application.data.entity;

import com.example.application.data.AbstractEntity;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.*;

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
@Table(name = "Evaluaciones")
public class Evaluacion extends AbstractEntity {

    @EqualsAndHashCode.Include
    @ToString.Include

    @NotEmpty
    @NotBlank(message = "campo vacío")
    @Column(name = "nota", nullable = false)
    @Size(message = "La nota es cualitativa (B,R,M)", max = 1, min = 1)
    @Pattern(regexp = "^(B|M|R)$", message = "Evaluación incorrecta")
    private String nota;

//    @NotEmpty
//    @NotBlank(message = "campo vacío")
    @Column(name = "descripcion", nullable = false)
    @Size(message = "La descripción no es correcta",max = 255,min=3)
    @Pattern(regexp = "^[a-zA-Z][a-zA-Z0-9\\s]*$", message = "Solo letras y numeros")
    private String descripcion;

    @NotNull(message = "debes elegir un campo")
    @JoinColumn(name = "estudiante_id")
    @ManyToOne()
    private Estudiante estudiante;

    @NotNull(message = "debes elegir un campo")
    @JoinColumn(name = "tarea_id")
    @ManyToOne()
    private Tarea tarea;

    @NotEmpty
    @NotBlank(message = "campo vacío")
    @Column(name = "status", nullable = false)
    private String status;

}
