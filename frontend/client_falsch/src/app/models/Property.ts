
import { Coding } from "./Coding";
import { CodeableConcept } from "./CodeableConcept";

export class Property{
constructor
(
    public type: CodeableConcept,
    public valueDate: Date = new Date(),
    public valueCodeableConcept: CodeableConcept,
    

){}

}