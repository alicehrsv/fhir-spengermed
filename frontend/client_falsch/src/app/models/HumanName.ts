export class HumanName {

  constructor(
    public use: string = "official",
    public text: string = "",
    public family: string = "",
    public given: string[] = [],
    public prefix: string[] = [],
    public suffix: string[] = [],

  )

  { }
}
