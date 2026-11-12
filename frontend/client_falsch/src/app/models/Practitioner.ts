import { Identifier } from './Identifier';
import { HumanName } from './HumanName';
import { ContactPoint } from './ContactPoint';
import { Address } from './Address';
import { Atachment } from './Atachment';
import { Qualification } from './Qualification';
import { CodeableConcept } from './CodeableConcept';

export class Practitioner {
  photo: any;
  constructor(
    public id?: string,
    public identifier: Identifier[] = [],
    public active: boolean=true,
    public name: HumanName[] = [],
    public telecom: ContactPoint[] = [],
    public gender?: string,
    public birthDate?: Date,
    public address: Address[] = [],
    public attachment: Atachment[] = [],
    public qualification: Qualification[] = [],
    public communication: CodeableConcept[] = []
  ) {}
}
