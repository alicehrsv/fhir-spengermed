package at.spengergasse.spengerfhirmed.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class VirtualServiceDetail extends Element {



    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "vsd_channel_type_id", referencedColumnName = "id")
    private Coding channelType;

    @Column(name = "vsd_address_url")
    private String addressUrl;

    @Column(name = "vsd_address_string")
    private String addressString;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "vsd_address_contact_point_id", referencedColumnName = "id")
    private ContactPoint addressContactPoint;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "vsd_address_extended_contact_detail_id", referencedColumnName = "id")
    private ExtendedContactDetail addressExtendedContactDetail;

    @ElementCollection
    @CollectionTable(name = "vsd_additional_info", joinColumns = @JoinColumn(name = "vsd_id"))
    @Column(name = "url")
    private List<String> additionalInfo = new ArrayList<>();

    @Positive
    @Column(name = "vsd_max_participants")
    private Integer maxParticipants;

    @Column(name = "vsd_session_key")
    private String sessionKey;
}