import { Identifier } from './Identifier';
import { CodeableConcept } from './CodeableConcept';
import { Reference } from './Reference';
import { Period } from './Period';

export class EncounterParticipant {
  constructor(
    public type: CodeableConcept[] = [],
    public period?: Period,
    public actor?: Reference
  ) {}
}

export class Encounter {
  constructor(
    public id?: string,
    public identifier: Identifier[] = [],
    public status: string = '',
    public priority?: CodeableConcept,
    public type: CodeableConcept[] = [],
    public serviceType: Reference[] = [],
    public subject?: Reference,
    public episodeOfCare: Reference[] = [],
    public basedOn: Reference[] = [],
    public careTeam: Reference[] = [],
    public serviceProvider?: Reference,
    public participant: EncounterParticipant[] = [], // Default leeres Array
    public appointment: Reference[] = [],
    public virtualService: any[] = [],
    public plannedStartDate?: string,
    public plannedEndDate?: string,
    public length?: any
  ) {}
}
