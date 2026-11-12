package at.spengergasse.spengerfhirmed.model;

import at.spengergasse.spengerfhirmed.model.Element;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="pe_period")
@Builder
public class Period extends Element {
    @Column(name = "pe_start")
    private LocalDateTime start;
    @Column(name = "pe_end")
    private LocalDateTime end;
}
