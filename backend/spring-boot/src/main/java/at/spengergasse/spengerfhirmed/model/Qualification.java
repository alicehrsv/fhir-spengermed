package at.spengergasse.spengerfhirmed.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "q_qualification")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Qualification extends BakcboneElement {

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_qu_id", referencedColumnName = "id")
    public List<Identifier> identifier = new ArrayList<Identifier>();

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="co_qu_id", referencedColumnName = "id")
    private Coding code;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="pe_qu_id", referencedColumnName = "id")
    private Period period;

}
