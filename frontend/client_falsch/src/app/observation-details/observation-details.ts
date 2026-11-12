import { Component, EventEmitter, Input, OnChanges, OnInit, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { DataService } from '../data.service';
import {
  Observation,
  ObservationStatus,
  ObservationReferenceRange,
  ObservationComponent,
  SimpleQuantity
} from '../models/Observation';
import { CodeableConcept } from '../models/CodeableConcept';
import { Quantity } from '../models/Quantity';
import { Annotation } from '../models/Annotation';

@Component({
  selector: 'app-observation-details',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './observation-details.html',
  styleUrls: ['./observation-details.scss']
})
export class ObservationDetailsComponent implements OnInit, OnChanges {

  @Input() id: string = '';
  @Output() observationModified = new EventEmitter<boolean>();

  observation: Observation | null = null;
  statuses = Object.values(ObservationStatus);
  valueType = '';

  constructor(private dataService: DataService) {}

  ngOnInit(): void {
    this.loadObservation();
  }

  ngOnChanges(): void {
    this.loadObservation();
  }

  loadObservation(): void {
    if (!this.id) return;

    this.dataService.getObservation(this.id)
      .subscribe({
        next: (obs) => {
          this.observation = obs;
          this.determineValueType();
          this.ensureObservationStructure();
        },
        error: (err) => console.error('Fehler beim Laden:', err)
      });
  }

  determineValueType(): void {
    if (!this.observation) return;

    if (this.observation.valueQuantity) {
      this.valueType = 'quantity';
    } else if (this.observation.valueString) {
      this.valueType = 'string';
    } else if (this.observation.valueBoolean !== undefined) {
      this.valueType = 'boolean';
    } else if (this.observation.valueInteger !== undefined) {
      this.valueType = 'integer';
    } else if (this.observation.valueCodeableConcept) {
      this.valueType = 'codeableConcept';
    } else {
      this.valueType = '';
    }
  }

  onValueTypeChange(): void {
    if (!this.observation) return;

    // Clear all value fields
    this.observation.valueQuantity = undefined;
    this.observation.valueString = undefined;
    this.observation.valueBoolean = undefined;
    this.observation.valueInteger = undefined;
    this.observation.valueCodeableConcept = undefined;

    // Initialize selected type
    switch (this.valueType) {
      case 'quantity':
        this.observation.valueQuantity = {
          value: 0,
          unit: '',
          system: 'http://unitsofmeasure.org',
          code: ''
        } as Quantity;
        break;
      case 'string':
        this.observation.valueString = '';
        break;
      case 'boolean':
        this.observation.valueBoolean = false;
        break;
      case 'integer':
        this.observation.valueInteger = 0;
        break;
      case 'codeableConcept':
        this.observation.valueCodeableConcept = {
          text: '',
          coding: []
        } as CodeableConcept;
        break;
    }
  }

  ensureObservationStructure(): void {
    if (!this.observation) return;

    // Ensure code exists
    if (!this.observation.code) {
      this.observation.code = { text: '', coding: [] } as CodeableConcept;
    }

    // Ensure subject exists
    if (!this.observation.subject) {
      this.observation.subject = { reference: '', display: '' };
    }

    // Ensure valueQuantity exists when needed
    if (this.valueType === 'quantity' && !this.observation.valueQuantity) {
      this.observation.valueQuantity = {
        value: 0,
        unit: '',
        system: 'http://unitsofmeasure.org',
        code: ''
      } as Quantity;
    }

    // Ensure valueCodeableConcept exists when needed
    if (this.valueType === 'codeableConcept' && !this.observation.valueCodeableConcept) {
      this.observation.valueCodeableConcept = {
        text: '',
        coding: []
      } as CodeableConcept;
    }

    // Ensure arrays exist
    if (!this.observation.referenceRange) {
      this.observation.referenceRange = [];
    }
    if (!this.observation.interpretation) {
      this.observation.interpretation = [];
    }
    if (!this.observation.note) {
      this.observation.note = [];
    }
    if (!this.observation.component) {
      this.observation.component = [];
    }

    // Ensure reference range has proper structure
    this.observation.referenceRange.forEach(range => {
      if (!range.low) {
        range.low = { value: 0, unit: '', system: 'http://unitsofmeasure.org', code: '' };
      }
      if (!range.high) {
        range.high = { value: 0, unit: '', system: 'http://unitsofmeasure.org', code: '' };
      }
    });

    // Ensure components have proper structure
    this.observation.component.forEach(comp => {
      if (!comp.code) {
        comp.code = { text: '', coding: [] } as CodeableConcept;
      }
      if (!comp.valueQuantity) {
        comp.valueQuantity = {
          value: 0,
          unit: '',
          system: 'http://unitsofmeasure.org',
          code: ''
        } as Quantity;
      }
    });
  }

  addReferenceRange(): void {
    if (!this.observation) return;

    const newRange: ObservationReferenceRange = {
      low: {
        value: 0,
        unit: '',
        system: 'http://unitsofmeasure.org',
        code: ''
      },
      high: {
        value: 0,
        unit: '',
        system: 'http://unitsofmeasure.org',
        code: ''
      },
      appliesTo: [],
      text: ''
    };

    this.observation.referenceRange.push(newRange);
  }

  addComponent(): void {
    if (!this.observation) return;

    const newComponent: ObservationComponent = {
      code: {
        text: '',
        coding: []
      } as CodeableConcept,
      valueQuantity: {
        value: 0,
        unit: '',
        system: 'http://unitsofmeasure.org',
        code: ''
      } as Quantity,
      interpretation: [],
      referenceRange: []
    };

    this.observation.component.push(newComponent);
  }

  addInterpretation(): void {
    if (!this.observation) return;

    this.observation.interpretation.push({
      text: '',
      coding: []
    } as CodeableConcept);
  }

  removeInterpretation(index: number): void {
    if (!this.observation) return;
    this.observation.interpretation.splice(index, 1);
  }

  addNote(): void {
    if (!this.observation) return;

    this.observation.note.push({
      text: '',
      time: new Date()
    } as Annotation);
  }

  removeNote(index: number): void {
    if (!this.observation) return;
    this.observation.note.splice(index, 1);
  }

  saveObservation(): void {
    if (!this.observation) return;

    this.dataService.saveObservation(this.observation)
      .subscribe({
        next: (saved) => {
          console.log('Observation gespeichert:', saved);
          this.observationModified.emit(true);
        },
        error: (err) => console.error('Fehler beim Speichern:', err)
      });
  }

  deleteObservation(): void {
    if (!this.observation || !confirm('Möchten Sie diese Observation wirklich löschen?')) {
      return;
    }

    this.dataService.deleteObservation(this.observation.id)
      .subscribe({
        next: () => {
          console.log('Observation gelöscht');
          this.observationModified.emit(true);
        },
        error: (err) => console.error('Fehler beim Löschen:', err)
      });
  }

  cancel(): void {
    this.observationModified.emit(false);
  }

  formatDateForInput(date: Date | undefined): string {
    if (!date) return '';
    const d = new Date(date);
    return d.toISOString().slice(0, 16);
  }

  parseDate(dateStr: string): Date | undefined {
    return dateStr ? new Date(dateStr) : undefined;
  }


  // --- strict template safe accessors ---

  get obs(): Observation {
    // Template hat *ngIf="observation", aber der Compiler weiß das nicht immer zuverlässig.
    return this.observation as Observation;
  }

  get obsCode(): CodeableConcept {
    this.obs.code ??= { text: '', coding: [] } as CodeableConcept;
    this.obs.code.text ??= '';
    this.obs.code.coding ??= [];
    return this.obs.code;
  }

  get obsSubject(): any {
    this.obs.subject ??= { reference: '', display: '' };
    (this.obs.subject as any).reference ??= '';
    (this.obs.subject as any).display ??= '';
    return this.obs.subject;
  }

  get obsValueQuantity(): Quantity {
    this.obs.valueQuantity ??= {
      value: 0,
      unit: '',
      system: 'http://unitsofmeasure.org',
      code: ''
    } as Quantity;

    (this.obs.valueQuantity as any).value ??= 0;
    (this.obs.valueQuantity as any).unit ??= '';
    (this.obs.valueQuantity as any).code ??= '';
    return this.obs.valueQuantity!;
  }

  get obsValueCodeableConcept(): CodeableConcept {
    this.obs.valueCodeableConcept ??= { text: '', coding: [] } as CodeableConcept;
    this.obs.valueCodeableConcept.text ??= '';
    this.obs.valueCodeableConcept.coding ??= [];
    return this.obs.valueCodeableConcept!;
  }

  getRangeLow(range: ObservationReferenceRange): any {
    range.low ??= { value: 0, unit: '', system: 'http://unitsofmeasure.org', code: '' };
    (range.low as any).value ??= 0;
    (range.low as any).unit ??= '';
    return range.low;
  }

  getRangeHigh(range: ObservationReferenceRange): any {
    range.high ??= { value: 0, unit: '', system: 'http://unitsofmeasure.org', code: '' };
    (range.high as any).value ??= 0;
    (range.high as any).unit ??= '';
    return range.high;
  }

  getCompCode(comp: ObservationComponent): CodeableConcept {
    comp.code ??= { text: '', coding: [] } as CodeableConcept;
    comp.code.text ??= '';
    comp.code.coding ??= [];
    return comp.code;
  }

  getCompValueQuantity(comp: ObservationComponent): Quantity {
    comp.valueQuantity ??= {
      value: 0,
      unit: '',
      system: 'http://unitsofmeasure.org',
      code: ''
    } as Quantity;

    (comp.valueQuantity as any).value ??= 0;
    (comp.valueQuantity as any).unit ??= '';
    (comp.valueQuantity as any).code ??= '';
    return comp.valueQuantity!;
  }

}
