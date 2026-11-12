import { r } from "./r";
import { data } from "./data";
import { Exn } from "./Exn";


export class PLF2_1 {

//post --> Datensatz kommt zurück


  constructor(
    public id: string,
    public r: r[] = [],
    public st: Code,
    public date: Date = new Date(),
  )

  {



  }

}
