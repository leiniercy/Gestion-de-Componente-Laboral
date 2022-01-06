package  com.example.application.data.entity;

import com.example.application.data.AbstractEntity;
import java.time.LocalDate;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
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
    @NotNull(message = "no puede estar vacio")
    @Column(name = "numero", unique = true)
    private Integer numero;

    @OneToMany(mappedBy = "grupo")
    List<Estudiante> estudiantes;

}
