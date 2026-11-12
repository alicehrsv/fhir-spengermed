import { Address } from "./Address";
import { Atachment } from "./Atachment";
import { ContactPoint } from "./ContactPoint";
import { HumanName } from "./HumanName";
import { Identifier } from "./Identifier";

export enum Gender{
    MALE = "male",
    FEMALE = "female",
    OTHER = "other",
    UNKNOWN = "unknown"

}

export class Patient {

//post --> Datensatz kommt zurück


  constructor(
    public id: string, // wird in der Datenbank generiert
    public name: HumanName[] = [],
    public gender: Gender = Gender.UNKNOWN,
    public active: boolean = false,
    public birthDate: Date = new Date(),
    public adresses: Address[] = [],
    public identifier: Identifier[] = [],
    public deceasedDateTime: Date = new Date(),
    public deceasedBoolean: boolean = false,
    public photo?: Atachment,
    public telecom: ContactPoint[] = [],
  )

    {



    }

}
