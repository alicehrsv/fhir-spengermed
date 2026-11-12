import { CodeableConcept } from './CodeableConcept';
import { Reference } from './Reference';
import { Quantity } from './Quantity';
import { Annotation } from './Annotation';

export enum ObservationStatus {
  REGISTERED = 'registered',
  PRELIMINARY = 'preliminary',
  FINAL = '_final',
  AMENDED = 'amended',
  CORRECTED = 'corrected',
  CANCELLED = 'cancelled',
  ENTERED_IN_ERROR = 'entered_in_error',
  UNKNOWN = 'unknown'
}

export class ObservationReferenceRange {
  constructor(
    public id?: string,
    public low?: SimpleQuantity,
    public high?: SimpleQuantity,
    public normalValue?: CodeableConcept,
    public type?: CodeableConcept,
    public appliesTo: CodeableConcept[] = [],
    public age?: Range,
    public text?: string
  ) {}
}

export class ObservationComponent {
  constructor(
    public id?: string,
    public code?: CodeableConcept,
    public valueQuantity?: Quantity,
    public valueCodeableConcept?: CodeableConcept,
    public valueString?: string,
    public valueBoolean?: boolean,
    public valueInteger?: number,
    public valueRange?: Range,
    public dataAbsentReason?: CodeableConcept,
    public interpretation: CodeableConcept[] = [],
    public referenceRange: ObservationReferenceRange[] = []
  ) {}
}

export class Observation {
  constructor(
    public id: string,
    public identifier: Identifier[] = [],
    public basedOn: Reference[] = [],
    public partOf: Reference[] = [],
    public status: ObservationStatus = ObservationStatus.FINAL,
    public category: CodeableConcept[] = [],
    public code?: CodeableConcept,
    public subject?: Reference,
    public focus: Reference[] = [],
    public encounter?: Reference,
    public effectiveDateTime?: Date,
    public effectivePeriod?: Period,
    public issued?: Date,
    public performer: Reference[] = [],
    public valueQuantity?: Quantity,
    public valueCodeableConcept?: CodeableConcept,
    public valueString?: string,
    public valueBoolean?: boolean,
    public valueInteger?: number,
    public valueRange?: Range,
    public dataAbsentReason?: CodeableConcept,
    public interpretation: CodeableConcept[] = [],
    public note: Annotation[] = [],
    public bodySite?: CodeableConcept,
    public bodyStructure?: Reference,
    public method?: CodeableConcept,
    public specimen?: Reference,
    public device?: Reference,
    public referenceRange: ObservationReferenceRange[] = [],
    public hasMember: Reference[] = [],
    public derivedFrom: Reference[] = [],
    public component: ObservationComponent[] = []
  ) {}
}

// Supporting interfaces
export interface SimpleQuantity {
  id?: string;
  value?: number;
  unit?: string;
  system?: string;
  code?: string;
}

export interface Range {
  id?: string;
  low?: SimpleQuantity;
  high?: SimpleQuantity;
}

export interface Period {
  id?: string;
  start?: Date;
  end?: Date;
}

export interface Identifier {
  id?: string;
  use?: string;
  type?: CodeableConcept;
  system?: string;
  value?: string;
  period?: Period;
}
