package at.spengergasse.spengerfhirmed.model;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="rep_repeat")
@Builder
public class Repeat extends Element{

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "rep_d_bounds_duration_id")
    private Duration boundsDuration;

    

}
