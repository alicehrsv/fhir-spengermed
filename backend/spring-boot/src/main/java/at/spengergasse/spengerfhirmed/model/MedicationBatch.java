package at.spengergasse.spengerfhirmed.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Entity
@Table(name = "mb_medication_batch")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class MedicationBatch extends BakcboneElement {

    @Column(name = "mb_lot_number")
    private String lotNumber;

    @Column(name = "mb_expiration_date")
    private LocalDate expirationDate;
}