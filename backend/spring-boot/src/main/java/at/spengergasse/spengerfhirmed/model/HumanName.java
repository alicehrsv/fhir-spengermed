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
@Table(name = "hn_human_name")
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class HumanName extends Element {

    public enum UseCode{
        usual , official , temp , nickname , anonymous , old , maiden
    }
    @Column(name = "hn_use")
    @Enumerated(EnumType.STRING)
    private UseCode use;
    @Column(name="hn_text")
    private String text;
    @Column(name="hn_family")
    private String family;
    @ElementCollection
    @CollectionTable(name = "hn_suffix", joinColumns = @JoinColumn(name = "hn_id"))
    private List<String> suffix = new ArrayList<String>();
    @ElementCollection
    @CollectionTable(name = "hn_given", joinColumns = @JoinColumn(name = "hn_id"))
    private List<String> given = new ArrayList<String>();
    @ElementCollection
    @CollectionTable(name = "hn_prefix", joinColumns = @JoinColumn(name = "hn_id"))
    private List<String> prefix = new ArrayList<String>();
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "hn_pe_id")
    private Period period;
}
