    package at.spengergasse.spengerfhirmed.model;

    import jakarta.persistence.*;
    import lombok.AllArgsConstructor;
    import lombok.Getter;
    import lombok.NoArgsConstructor;
    import lombok.Setter;
    import lombok.experimental.SuperBuilder;

    @Entity
    @Table(name = "r_ratio")
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @SuperBuilder
    public class Ratio extends Element {

        @OneToOne(cascade = CascadeType.ALL)
        @JoinColumn(name = "r_numerator_id")
        private Quantity numerator;

        @OneToOne(cascade = CascadeType.ALL)
        @JoinColumn(name = "r_denominator_id")
        private Quantity denominator;
    }