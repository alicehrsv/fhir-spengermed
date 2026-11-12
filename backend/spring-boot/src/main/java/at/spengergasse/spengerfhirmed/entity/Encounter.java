package at.spengergasse.spengerfhirmed.entity;

import at.spengergasse.spengerfhirmed.model.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "e_encounter")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Encounter extends DomainResource {

    public enum Status {
        planned,
        in_progress("in-progress"),
        on_hold("on-hold"),
        discharged,
        completed,
        cancelled,
        discontinued,
        entered_in_error("entered-in-error"),
        unknown;

        private final String value;

        Status() {
            this.value = this.name().replace('_', '-');
        }

        Status(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "i_e_id", referencedColumnName = "id")
    private List<Identifier> identifier = new ArrayList<Identifier>();

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "e_status", nullable = false)
    private Status status;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "cc_priority_id", referencedColumnName = "id")
    private CodeableConcept priority;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "cc_type_e_id", referencedColumnName = "id")
    private List<CodeableConcept> type = new ArrayList<CodeableConcept>();

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "cr_service_e_id", referencedColumnName = "id")
    private List<CodeableReference> serviceType = new ArrayList<CodeableReference>();

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "r_subject_id", referencedColumnName = "id")
    private Reference subject;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "r_episode_e_id", referencedColumnName = "id")
    private List<Reference> episodeOfCare = new ArrayList<Reference>();

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "r_based_e_id", referencedColumnName = "id")
    private List<Reference> basedOn = new ArrayList<Reference>();

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "r_careteam_e_id", referencedColumnName = "id")
    private List<Reference> careTeam = new ArrayList<Reference>();

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "r_service_provider_id", referencedColumnName = "id")
    private Reference serviceProvider;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "ep_e_id", referencedColumnName = "id")
    private List<Participant> participant = new ArrayList<Participant>();

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "r_appointment_e_id", referencedColumnName = "id")
    private List<Reference> appointment = new ArrayList<Reference>();

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "vsd_e_id", referencedColumnName = "id")
    private List<VirtualServiceDetail> virtualService = new ArrayList<VirtualServiceDetail>();

    @Column(name = "e_planned_start_date")
    private LocalDateTime plannedStartDate;

    @Column(name = "e_planned_end_date")
    private LocalDateTime plannedEndDate;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "d_length_id", referencedColumnName = "id")
    private Duration length;
}