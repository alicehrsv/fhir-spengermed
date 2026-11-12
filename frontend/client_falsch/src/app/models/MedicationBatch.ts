export class MedicationBatch {
  constructor(
    public lotNumber: string[] = [''],   // wichtig für UI Bindings
    public expirationDate: string = ''   // als String für HTML <input type="date">
  ) {}
}
