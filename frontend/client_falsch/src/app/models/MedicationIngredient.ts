import { CodeableConcept } from './CodeableConcept';
import { Reference } from './Reference';
import { Ratio } from './Ratio';

export class MedicationIngredient {
  constructor(
    public itemCodeableConcept: CodeableConcept = new CodeableConcept(),
    public itemReference: Reference = new Reference(),
    public isActive: boolean = true,
    public strength: Ratio = new Ratio()
  ) {}
}
