import {CodeableConcept} from './CodeableConcept';

export class agent {
  constructor(
    public type: CodeableConcept = new CodeableConcept()
  )
}
