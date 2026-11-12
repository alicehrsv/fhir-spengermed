import { Identifier } from './Identifier';
import { CodeableConcept } from './CodeableConcept';
import { Reference } from './Reference';
import { Ratio } from './Ratio';
import { MedicationIngredient } from './MedicationIngredient';
import { MedicationBatch } from './MedicationBatch';

export enum MedicationStatus {
  ACTIVE = 'active',
  INACTIVE = 'inactive',
  ENTERED_IN_ERROR = 'entered_in_error'
}

export class Medication {
  public id: string = '';
  public identifier: Identifier[] = [];
  public code: CodeableConcept = new CodeableConcept();
  public status?: MedicationStatus;
  public manufacturer: Reference = new Reference();
  public form: CodeableConcept = new CodeableConcept();
  public amount: Ratio = new Ratio();
  public ingredient: MedicationIngredient[] = [];
  public batch: MedicationBatch = new MedicationBatch();

  constructor() {
    // Sicherstellen, dass alle Objekte immer initialisiert sind
    this.code = new CodeableConcept();
    this.manufacturer = new Reference();
    this.form = new CodeableConcept();
    this.amount = new Ratio();
    this.batch = new MedicationBatch();
    this.identifier = [];
    this.ingredient = [];
  }
}
