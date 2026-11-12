package at.spengergasse.spengerfhirmed.model;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="ti_timing")
@Builder
public class Timing extends Element{

    @ElementCollection
    @CollectionTable(name = "event_timing", joinColumns = @JoinColumn(name = "id"))
    private List<LocalDateTime> eventTiming = new ArrayList<>();


}
