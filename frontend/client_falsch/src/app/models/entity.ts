

export enum rolecode{
  SOURCE= "source"
}
export class entity {

  constructor(
    public id: string, // wird in der Datenbank generiert,
    public rolecode: rolecode = rolecode.source
  )
}
