package at.spengergasse.spengerfhirmed.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tr_triggeredby")
@Getter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class TriggeredBy extends BakcboneElement {

    @NotNull
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "tr_re_id", nullable =false )
    private Reference observation;

    public enum Type{refelex, repeat,@JsonProperty("re-run") re_run}
    @Enumerated(EnumType.STRING)
    @Column(name = "tr_type", nullable = false)
    private Type type;

    @Column(name="tr_reason")
    private String reason;


}
