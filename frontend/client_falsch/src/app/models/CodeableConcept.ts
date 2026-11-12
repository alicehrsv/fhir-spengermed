import { Coding } from './Coding';

export class CodeableConcept {
  public coding: any[] = []; // Default leeres Array
  public text?: string = '';
  public id?: string;

  constructor(coding?: Coding[], text: string = '', id?: string) {
    this.coding = coding ?? [];
    this.text = text;
    this.id = id;
  }
}
