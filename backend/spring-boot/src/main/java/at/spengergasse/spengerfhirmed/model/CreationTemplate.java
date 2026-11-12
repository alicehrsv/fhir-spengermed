package at.spengergasse.spengerfhirmed.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.Date;

@Entity
@Table(name = "ct_tabelle")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class CreationTemplate extends BakcboneElement {

    @Column(name = "ct_lastCreationDate")
    private Date lastCreationDate;


    @Column(name = "ct_creationCount")
    private Integer creationCount;


}
