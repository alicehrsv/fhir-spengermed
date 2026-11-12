package at.spengergasse.spengerfhirmed.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "re_reference")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder

public class Reference extends Element {
    @Column(name = "re_reference")
    private String reference;

    @Column(name = "re_type")
    private String type;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "re_id_id")

    private Identifier identifier;

    @Column(name = "re_display")
    private String display;


}
