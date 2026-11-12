package at.spengergasse.spengerfhirmed.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "co_coding")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class SimpleQuantity extends BakcboneElement {
    @Column(name = "co_system")
    private String system;
    @Column(name = "co_version")
    private String version;
    @Column(name = "co_code")
    private String code;
    @Column(name = "co_display")
    private String display;
    @Column(name = "co_userselected")
    private boolean userSelected;
}
