export class Address {
  constructor(
    public use: string = "home",
    public type: string = "both",
    public text: string = "",
    public line: string[] = [],
    public city: string = "",
    public district: string = "",
    public state: string = "",
    public postalCode: string = "",
    public country: string = "",
  ) { }
}
