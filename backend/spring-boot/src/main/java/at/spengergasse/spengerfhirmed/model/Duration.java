package at.spengergasse.spengerfhirmed.model;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.AssertTrue;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="d_duration")
@SuperBuilder
public class Duration extends Quantity {

    @Column(name = "d_unit", nullable = false)
    private String unit;

    @AssertTrue(message = "Unit must be a valid unit of time")
    private boolean isUnitValid(){
        return unit.equals("s") || unit.equals("min") || unit.equals("h") || unit.equals("d") || unit.equals("wk") || unit.equals("mo") || unit.equals("a");
    }

}
