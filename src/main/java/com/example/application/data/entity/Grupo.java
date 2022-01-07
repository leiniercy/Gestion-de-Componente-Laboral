package  com.example.application.data.entity;

import com.example.application.data.AbstractEntity;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "Sample_Persons")
public class Grupo extends AbstractEntity {

    @EqualsAndHashCode.Include
    @ToString.Include
    
    @NotBlank(message = "no puede estar vacio")
    @Column(name = "numero", unique = true)
    @Size(max = 4, min = 4)
    private String numero;

    @OneToMany(mappedBy = "grupo")
    List<Estudiante> estudiantes;

}
