package at.spengergasse.spengerfhirmed.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.UuidGenerator;

@MappedSuperclass
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public abstract class Resource {

    public enum LANGUAGE_CODE {
        @JsonProperty("de-AT") de_AT,
        @JsonProperty("de-DE") de_DE,
        @JsonProperty("en-GB") en_GB,
        @JsonProperty("en-US") en_US,
        @JsonProperty("fr-FR") fr_FR,
        @JsonProperty("it-IT") it_IT,
        @JsonProperty("es-ES") es_ES,
        @JsonProperty("ru-RU") ru_RU,
        @JsonProperty("zh-CN") zh_CN,
        @JsonProperty("ja-JP") ja_JP,
        @JsonProperty("ko-KR") ko_KR,
        @JsonProperty("ar-SA") ar_SA,
        @JsonProperty("hi-IN") hi_IN,
        @JsonProperty("nl-NL") nl_NL
    }

    @Id
    @UuidGenerator
    @Column(name = "id", length = 36, updatable = false, nullable = false)
    private String id;

    @PrePersist
    void prePersist() {
        // falls Client "" schickt -> damit Hibernate UUID generieren darf
        if (id != null && id.isBlank()) {
            id = null;
        }
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "re_language")
    private LANGUAGE_CODE language;
}

