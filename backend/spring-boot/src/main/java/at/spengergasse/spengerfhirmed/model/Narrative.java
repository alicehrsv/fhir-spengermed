    package at.spengergasse.spengerfhirmed.model;

    import jakarta.persistence.*;
    import jakarta.validation.constraints.NotNull;
    import lombok.*;
    import lombok.experimental.SuperBuilder;

    @Entity
    @Table(name = "na_narrative")
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @SuperBuilder
    public class Narrative extends Element{
        public enum NarrativeCode{
            generated , extensions , additional , empty
        }
        @Enumerated(EnumType.STRING)
        @Column(name= "n_status", nullable = false)
        private NarrativeCode status;
        @Lob
        @Column(name="n_div",nullable = false, columnDefinition = "TEXT")
        private String div;

    }
