import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { DataService } from '../data.service';
import { Observation, ObservationStatus } from '../models/Observation';
import { CodeableConcept } from '../models/CodeableConcept';
import { Reference } from '../models/Reference';
import { Quantity } from '../models/Quantity';
import { Bundle, BundleEntry } from '../models/Bundle';
import { ObservationDetailsComponent } from '../observation-details/observation-details';

@Component({
  selector: 'app-observation-list',
  standalone: true,
  imports: [CommonModule, FormsModule, ObservationDetailsComponent],
  templateUrl: './observation-list.html',
  styleUrls: ['./observation-list.scss']
})
export class ObservationListComponent implements OnInit {

  observations: Observation[] = [];
  selectedObservation: Observation | null = null;
  showAddForm = false;
  newObservation: Observation = this.createEmptyObservation();

  // Form helpers for adding new observation
  codeText = '';
  subjectRef = '';
  effectiveDateTimeStr = '';
  valueNumber: number | null = null;
  valueUnit = '';
  valueCode = '';

  statuses = Object.values(ObservationStatus);

  searchParams = {
    status: '',
    code: '',
    subject: '',
    category: '',
    date: '',
    'value-quantity': '',
    _count: 10,
    _page: 1
  };

  total = 0;
  Math = Math; // Make Math available in template

  constructor(private dataService: DataService) {}

  ngOnInit() {
    this.searchObservations();
  }

  searchObservations() {
    // Filter empty parameters
    const cleanedParams: any = {};
    Object.keys(this.searchParams).forEach(key => {
      const value = (this.searchParams as any)[key];
      if (value !== '' && value !== null && value !== undefined) {
        cleanedParams[key] = value;
      }
    });

    this.dataService.searchObservations(cleanedParams)
      .subscribe({
        next: (bundle) => {
          this.observations = bundle.entry
            ? bundle.entry.map((e: BundleEntry) => e.resource as Observation)
            : [];
          this.total = bundle.total || 0;
        },
        error: (err) => {
          console.error('Fehler bei der Suche:', err);
          this.observations = [];
          this.total = 0;
        }
      });
  }

  selectObservation(obs: Observation) {
    this.selectedObservation = obs;
    this.showAddForm = false;
  }

  onObservationModified(reload: boolean) {
    if (reload) {
      this.searchObservations();
      this.selectedObservation = null;
    }
  }

  toggleAddForm() {
    this.showAddForm = !this.showAddForm;
    if (this.showAddForm) {
      this.selectedObservation = null;
      this.resetAddForm();
    }
  }

  addObservation() {
    // Build observation from form
    const observation: Observation = {
      id: " ",
      status: this.newObservation.status,
      code: {
        text: this.codeText,
        coding: []
      } as CodeableConcept,
      subject: {
        reference: this.subjectRef
      } as Reference,
      effectiveDateTime: this.effectiveDateTimeStr
        ? new Date(this.effectiveDateTimeStr)
        : undefined,
      valueQuantity: (this.valueNumber !== null && this.valueUnit) ? {
        value: this.valueNumber,
        unit: this.valueUnit,
        system: 'http://unitsofmeasure.org',
        code: this.valueCode || this.valueUnit
      } as Quantity : undefined,
      identifier: [],
      basedOn: [],
      partOf: [],
      category: [],
      focus: [],
      performer: [],
      interpretation: [],
      note: [],
      referenceRange: [],
      hasMember: [],
      derivedFrom: [],
      component: []
    };

    this.dataService.addObservation(observation)
      .subscribe({
        next: (saved) => {
          console.log('Observation gespeichert:', saved);
          this.searchObservations();
          this.showAddForm = false;
          this.resetAddForm();
        },
        error: (err) => console.error('Fehler beim Speichern:', err)
      });
  }

  /**
   * Löscht eine einzelne Observation
   */
  deleteSingleObservation(id: string) {
    if (!confirm('Möchten Sie diese Observation wirklich löschen?')) {
      return;
    }

    this.dataService.deleteObservation(id)
      .subscribe({
        next: () => {
          console.log('Observation gelöscht:', id);
          // Schließe Details-Ansicht wenn die gerade angezeigte gelöscht wurde
          if (this.selectedObservation?.id === id) {
            this.selectedObservation = null;
          }
          this.searchObservations();
        },
        error: (err) => {
          console.error('Fehler beim Löschen:', err);
          alert('Fehler beim Löschen der Observation');
        }
      });
  }

  /**
   * Löscht ALLE Observations (mit Warnung)
   */
  deleteAllObservations() {
    const confirmMessage = `WARNUNG: Sie sind dabei, ALLE ${this.total} Observations zu löschen!\n\nDiese Aktion kann nicht rückgängig gemacht werden.\n\nMöchten Sie wirklich fortfahren?`;

    if (!confirm(confirmMessage)) {
      return;
    }

    // Zweite Bestätigung
    if (!confirm('Sind Sie ABSOLUT SICHER? Dies löscht alle Daten unwiderruflich!')) {
      return;
    }

    this.dataService.deleteAllObservations()
      .subscribe({
        next: () => {
          console.log('Alle Observations gelöscht');
          this.selectedObservation = null;
          this.observations = [];
          this.total = 0;
          alert('Alle Observations wurden gelöscht');
        },
        error: (err) => {
          console.error('Fehler beim Löschen aller Observations:', err);
          alert('Fehler beim Löschen aller Observations');
        }
      });
  }

  resetAddForm() {
    this.newObservation = this.createEmptyObservation();
    this.codeText = '';
    this.subjectRef = '';
    this.effectiveDateTimeStr = '';
    this.valueNumber = null;
    this.valueUnit = '';
    this.valueCode = '';
  }

  resetSearch() {
    this.searchParams = {
      status: '',
      code: '',
      subject: '',
      category: '',
      date: '',
      'value-quantity': '',
      _count: 10,
      _page: 1
    };
    this.searchObservations();
  }

  nextPage() {
    if (this.searchParams._page * this.searchParams._count < this.total) {
      this.searchParams._page++;
      this.searchObservations();
    }
  }

  prevPage() {
    if (this.searchParams._page > 1) {
      this.searchParams._page--;
      this.searchObservations();
    }
  }

  getStatusBadgeClass(status: ObservationStatus): string {
    switch (status) {
      case ObservationStatus.FINAL:
        return 'bg-success';
      case ObservationStatus.PRELIMINARY:
        return 'bg-warning';
      case ObservationStatus.AMENDED:
      case ObservationStatus.CORRECTED:
        return 'bg-info';
      case ObservationStatus.CANCELLED:
      case ObservationStatus.ENTERED_IN_ERROR:
        return 'bg-danger';
      default:
        return 'bg-secondary';
    }
  }

  hasValue(obs: Observation): boolean {
    return !!(obs.valueQuantity || obs.valueString ||
      obs.valueCodeableConcept || obs.valueBoolean !== undefined ||
      obs.valueInteger !== undefined || obs.valueRange);
  }

  private createEmptyObservation(): Observation {
    return new Observation(
      '',
      [], // identifier
      [], // basedOn
      [], // partOf
      ObservationStatus.FINAL,
      [], // category
      undefined, // code
      undefined, // subject
      [], // focus
      undefined, // encounter
      undefined, // effectiveDateTime
      undefined, // effectivePeriod
      undefined, // issued
      [], // performer
      undefined, // valueQuantity
      undefined, // valueCodeableConcept
      undefined, // valueString
      undefined, // valueBoolean
      undefined, // valueInteger
      undefined, // valueRange
      undefined, // dataAbsentReason
      [], // interpretation
      [], // note
      undefined, // bodySite
      undefined, // bodyStructure
      undefined, // method
      undefined, // specimen
      undefined, // device
      [], // referenceRange
      [], // hasMember
      [], // derivedFrom
      []  // component
    );
  }
}
