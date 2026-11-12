
import { data } from "./data";


export enum type{
  DENY = "deny",
  PERMIT = "permit",
}

export class r{



  constructor(
    public id: string, // wird in der Datenbank generiert
    public type: Code = type.DENY,
    public data: Data[] = [],



  )

}
