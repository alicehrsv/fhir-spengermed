// models/Ratio.ts
import { Quantity } from './Quantity';

export class Ratio {
  public id?: string;
  public numerator: Quantity = new Quantity();
  public denominator: Quantity = new Quantity();

  constructor(init?: Partial<Ratio>) {
    this.id = init?.id;
    this.numerator = init?.numerator ? new Quantity(init.numerator) : new Quantity();
    this.denominator = init?.denominator ? new Quantity(init.denominator) : new Quantity();
  }
}
