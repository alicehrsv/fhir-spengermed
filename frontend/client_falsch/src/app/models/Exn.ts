


export enum tL{
  TEXT_CQL= "text_cql", TEXT_FHIRPATH="text_fhirpath", APPLICATION_X_FHIR_QEUREY= "application_x_fhir_query", ETC="etc"
}
export class Exn {

constructor(
  public id: string, // wird in der Datenbank generiert,
  public tL: tL = tL.TEXT_CQL,
  public en: String = new String()
)
}
