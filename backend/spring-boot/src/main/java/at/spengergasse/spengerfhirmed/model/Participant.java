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
@Table(name = "ep_encounter_participant")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Participant extends BakcboneElement {

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "cc_ep_id", referencedColumnName = "id")
    private List<CodeableConcept> type = new ArrayList<CodeableConcept>();

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "pe_ep_id", referencedColumnName = "id")
    private Period period;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "r_ep_id", referencedColumnName = "id")
    private Reference actor;
}