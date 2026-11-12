package at.spengergasse.spengerfhirmed.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="q_quantity")
@SuperBuilder
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Quantity extends Element{

    public enum ComparatorCode {
        bigger(">"),
        biggerOrequal(">="),
        smaller("<"),
        smallerOrEqual("<=");

        private String value;

        private ComparatorCode(String value){this.value = value;}


            public String toString(){
                return this.value;
            }


    }

    @Column(name = "q_value")
    protected double value;

    @Enumerated(EnumType.STRING)
    @Column(name = "q_comparator")
    protected ComparatorCode comparator;

    @Column(name = "q_unit")
    protected String unit;

    @Column(name = "q_system")
    protected String system;

    @Column(name = "q_code")
    protected String code;





}
