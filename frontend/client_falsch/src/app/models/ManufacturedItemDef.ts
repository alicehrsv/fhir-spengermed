import { Identifier } from "./Identifier";
import { CodeableConcept } from "./CodeableConcept";
import { Property } from "./Property";



export enum Status{
    DRAFT = "draft",
    ACTIVE = "active",
    RETIRED = "retired",
    UNKNOWN = "unknown"
        
}
export class ManufacturedItemDefinition {



   constructor(
        public id: string, 
        public identifier: Identifier[] = [],
        public status: Status = Status.UNKNOWN,
        public manufacturedDoseForm: CodeableConcept,
        public property: Property [] = []
        
   ) 
   {}

}