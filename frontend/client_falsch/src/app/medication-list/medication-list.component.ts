import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { DataService } from '../data.service';

import { MedicationDetailsComponent } from '../medication-details/medication-details.component';
import { Medication, MedicationStatus } from '../models/Medication';
import { CodeableConcept } from '../models/CodeableConcept';
import { Ratio } from '../models/Ratio';
import { MedicationBatch } from '../models/MedicationBatch';
import { Quantity } from '../models/Quantity';
import { Bundle } from '../models/Bundle';

@Component({
  selector: 'app-medicationlist',
  standalone: true,
  imports: [CommonModule, FormsModule, MedicationDetailsComponent],
  templateUrl: './medication-list.component.html',
  styleUrls: ['./medication-list.component.scss'],
})
export class MedicationListComponent implements OnInit {
  // UI state
  public isLoading = false;
  public errorMessage = '';
  public showAddForm = false;
  public isEditMode = false;

  // Data
  public medications: Medication[] = [];
  public selectedMedication: Medication = this.createEmptyMedication();
  public selectedMedicationId: string = '';
  public medicationStatuses: string[] = Object.values(MedicationStatus);

  // Search & Pagination
  public searchParams = {
    _count: 10,
    _page: 1
  };
  public total = 0;
  public Math = Math; // Für Template-Zugriff

  constructor(private dataService: DataService) {}

  ngOnInit(): void {
    this.searchMedications();
  }

  // ---------- load & helpers ----------
  public load(): void {
    this.searchMedications();
  }

  public searchMedications(): void {
    this.isLoading = true;
    this.errorMessage = '';

    // Filtere leere Parameter heraus
    const filteredParams: any = {};
    Object.entries(this.searchParams).forEach(([key, value]) => {
      if (value !== null && value !== undefined) {
        filteredParams[key] = value;
      }
    });

    this.dataService.searchMedications(filteredParams).subscribe({
      next: (bundle) => {
        this.medications = bundle.entry
          ? bundle.entry.map(e => this.ensureMedicationStructure(e.resource))
          : [];
        this.total = bundle.total || 0;
        this.isLoading = false;
      },
      error: (error) => {
        console.error('Error loading medications:', error);
        this.errorMessage = String(error?.message ?? 'Fehler beim Laden der Medikamente');
        this.medications = [];
        this.total = 0;
        this.isLoading = false;
      }
    });
  }

  public resetSearch(): void {
    this.searchParams = {
      _count: 10,
      _page: 1
    };
    this.searchMedications();
  }

  public nextPage(): void {
    this.searchParams._page++;
    this.searchMedications();
  }

  public prevPage(): void {
    if (this.searchParams._page > 1) {
      this.searchParams._page--;
      this.searchMedications();
    }
  }

  private createEmptyMedication(): Medication {
    const med = new Medication();
    med.code = new CodeableConcept();
    med.code.text = '';
    med.code.coding = [{
      system: 'http://www.nlm.nih.gov/research/umls/rxnorm',
      code: '',
      display: ''
    }];

    med.form = new CodeableConcept();
    med.form.text = '';
    med.form.coding = [{
      system: 'http://snomed.info/sct',
      code: '',
      display: ''
    }];

    med.status = MedicationStatus.ACTIVE;

    // WICHTIG: Amount mit gültigen Werten initialisieren
    med.amount = new Ratio();
    med.amount.numerator = {
      value: 1,
      unit: 'mg',
      system: 'http://unitsofmeasure.org',
      code: 'mg'
    } as Quantity;

    med.amount.denominator = {
      value: 1,
      unit: 'tablet',
      system: 'http://unitsofmeasure.org',
      code: 'tablet'
    } as Quantity;

    med.batch = new MedicationBatch();
    med.batch.lotNumber = [''];
    med.batch.expirationDate = '';

    med.manufacturer = { reference: '', display: '' };
    med.identifier = [];
    med.ingredient = [];
    med.id = '';

    return med;
  }

  private ensureMedicationStructure(m: Medication): Medication {
    // Ensure code
    m.code = m.code ?? new CodeableConcept();
    m.code.text = m.code.text ?? '';
    if (!m.code.coding || m.code.coding.length === 0) {
      m.code.coding = [{
        system: 'http://www.nlm.nih.gov/research/umls/rxnorm',
        code: m.code.text || 'unknown',
        display: m.code.text || 'unknown'
      }];
    }

    // Ensure form
    m.form = m.form ?? new CodeableConcept();
    m.form.text = m.form.text ?? '';
    if (!m.form.coding || m.form.coding.length === 0) {
      m.form.coding = [{
        system: 'http://snomed.info/sct',
        code: m.form.text || 'unknown',
        display: m.form.text || 'unknown'
      }];
    }

    // KRITISCH: Amount mit gültigen Werten
    m.amount = m.amount ?? new Ratio();

    if (!m.amount.numerator || m.amount.numerator.value === null || m.amount.numerator.value === undefined) {
      m.amount.numerator = {
        value: 1,
        unit: 'mg',
        system: 'http://unitsofmeasure.org',
        code: 'mg'
      } as Quantity;
    } else {
      m.amount.numerator.value = m.amount.numerator.value ?? 1;
      m.amount.numerator.unit = m.amount.numerator.unit || 'mg';
      m.amount.numerator.system = m.amount.numerator.system || 'http://unitsofmeasure.org';
      m.amount.numerator.code = m.amount.numerator.code || 'mg';
    }

    if (!m.amount.denominator || m.amount.denominator.value === null || m.amount.denominator.value === undefined) {
      m.amount.denominator = {
        value: 1,
        unit: 'tablet',
        system: 'http://unitsofmeasure.org',
        code: 'tablet'
      } as Quantity;
    } else {
      m.amount.denominator.value = m.amount.denominator.value ?? 1;
      m.amount.denominator.unit = m.amount.denominator.unit || 'tablet';
      m.amount.denominator.system = m.amount.denominator.system || 'http://unitsofmeasure.org';
      m.amount.denominator.code = m.amount.denominator.code || 'tablet';
    }

    // Batch
    m.batch = m.batch ?? new MedicationBatch();
    if (!m.batch.lotNumber || m.batch.lotNumber.length === 0) {
      m.batch.lotNumber = ['unknown'];
    }
    m.batch.expirationDate = m.batch.expirationDate ?? '';

    // Manufacturer
    m.manufacturer = m.manufacturer ?? { reference: '', display: '' };
    if (!m.manufacturer.reference || m.manufacturer.reference.trim() === '') {
      const display = m.manufacturer.display || 'unknown';
      m.manufacturer.reference = `Organization/${display.toLowerCase().replace(/\s+/g, '-')}`;
    }

    m.status = m.status ?? MedicationStatus.ACTIVE;
    m.identifier = m.identifier ?? [];
    m.ingredient = m.ingredient ?? [];
    m.id = m.id ?? '';

    return m;
  }

  public trackByMedicationId(index: number, medication: Medication): string {
    return medication.id || index.toString();
  }

  // ---------- Clean medication for server ----------
  private cleanMedicationForServer(med: Medication): any {
    const cleaned: any = {
      status: med.status || 'active'
    };

    // ID nur wenn vorhanden
    if (med.id && med.id.trim() !== '') {
      cleaned.id = med.id;
    }

    // Code: text ist Pflicht, coding nur wenn code gefüllt ist
    cleaned.code = {
      text: med.code?.text || 'unknown'
    };
    if (med.code?.coding && med.code.coding.length > 0) {
      const validCodings = med.code.coding.filter(c => c.code && c.code.trim() !== '');
      if (validCodings.length > 0) {
        cleaned.code.coding = validCodings;
      } else {
        // Erstelle gültiges coding aus text
        cleaned.code.coding = [{
          system: 'http://www.nlm.nih.gov/research/umls/rxnorm',
          code: med.code.text || 'unknown',
          display: med.code.text || 'unknown'
        }];
      }
    } else {
      cleaned.code.coding = [{
        system: 'http://www.nlm.nih.gov/research/umls/rxnorm',
        code: med.code?.text || 'unknown',
        display: med.code?.text || 'unknown'
      }];
    }

    // Form: nur hinzufügen wenn text vorhanden
    if (med.form?.text && med.form.text.trim() !== '') {
      cleaned.form = {
        text: med.form.text
      };

      const validFormCodings = med.form.coding?.filter(c => c.code && c.code.trim() !== '');
      if (validFormCodings && validFormCodings.length > 0) {
        cleaned.form.coding = validFormCodings;
      } else {
        cleaned.form.coding = [{
          system: 'http://snomed.info/sct',
          code: med.form.text,
          display: med.form.text
        }];
      }
    }

    // Amount: nur wenn Werte vorhanden
    if (med.amount?.numerator?.value && med.amount?.denominator?.value) {
      cleaned.amount = {
        numerator: {
          value: med.amount.numerator.value,
          unit: med.amount.numerator.unit || 'mg',
          system: med.amount.numerator.system || 'http://unitsofmeasure.org',
          code: med.amount.numerator.code || med.amount.numerator.unit || 'mg'
        },
        denominator: {
          value: med.amount.denominator.value,
          unit: med.amount.denominator.unit || 'tablet',
          system: med.amount.denominator.system || 'http://unitsofmeasure.org',
          code: med.amount.denominator.code || med.amount.denominator.unit || 'tablet'
        }
      };
    }

    // Manufacturer: nur wenn reference ODER display vorhanden
    if (med.manufacturer?.reference && med.manufacturer.reference.trim() !== '') {
      cleaned.manufacturer = {
        reference: med.manufacturer.reference
      };
      if (med.manufacturer.display && med.manufacturer.display.trim() !== '') {
        cleaned.manufacturer.display = med.manufacturer.display;
      }
    } else if (med.manufacturer?.display && med.manufacturer.display.trim() !== '') {
      cleaned.manufacturer = {
        reference: `Organization/${med.manufacturer.display.toLowerCase().replace(/\s+/g, '-')}`,
        display: med.manufacturer.display
      };
    }

    // Batch: nur wenn lotNumber oder expirationDate vorhanden
    const validLotNumbers = med.batch?.lotNumber?.filter(lot => lot && lot.trim() !== '') || [];
    if (validLotNumbers.length > 0 || (med.batch?.expirationDate && med.batch.expirationDate.trim() !== '')) {
      cleaned.batch = {};

      if (validLotNumbers.length > 0) {
        cleaned.batch.lotNumber = validLotNumbers;
      }

      if (med.batch?.expirationDate && med.batch.expirationDate.trim() !== '') {
        cleaned.batch.expirationDate = med.batch.expirationDate;
      }
    }

    // Identifier: nur gültige
    const validIdentifiers = med.identifier?.filter(id =>
      id.system && id.system.trim() !== '' && id.value && id.value.trim() !== ''
    ) || [];
    if (validIdentifiers.length > 0) {
      cleaned.identifier = validIdentifiers;
    }

    // Ingredients: nur gültige
    const validIngredients = med.ingredient?.filter(ing =>
      ing.itemCodeableConcept?.text && ing.itemCodeableConcept.text.trim() !== ''
    ) || [];
    if (validIngredients.length > 0) {
      cleaned.ingredient = validIngredients.map(ing => {
        const cleanedIng: any = {
          itemCodeableConcept: {
            text: ing.itemCodeableConcept.text
          }
        };

        // Coding für Ingredient
        const validIngCodings = ing.itemCodeableConcept.coding?.filter(c => c.code && c.code.trim() !== '');
        if (validIngCodings && validIngCodings.length > 0) {
          cleanedIng.itemCodeableConcept.coding = validIngCodings;
        } else {
          cleanedIng.itemCodeableConcept.coding = [{
            system: 'http://www.nlm.nih.gov/research/umls/rxnorm',
            code: ing.itemCodeableConcept.text,
            display: ing.itemCodeableConcept.text
          }];
        }

        // Strength
        if (ing.strength?.numerator?.value && ing.strength?.denominator?.value) {
          cleanedIng.strength = {
            numerator: {
              value: ing.strength.numerator.value,
              unit: ing.strength.numerator.unit || 'mg',
              system: ing.strength.numerator.system || 'http://unitsofmeasure.org',
              code: ing.strength.numerator.code || ing.strength.numerator.unit || 'mg'
            },
            denominator: {
              value: ing.strength.denominator.value,
              unit: ing.strength.denominator.unit || 'tablet',
              system: ing.strength.denominator.system || 'http://unitsofmeasure.org',
              code: ing.strength.denominator.code || ing.strength.denominator.unit || 'tablet'
            }
          };
        }

        return cleanedIng;
      });
    }

    return cleaned;
  }

  // ---------- list interactions ----------
  public onMedicationModified(refresh: any): void {
    const doRefresh = typeof refresh === 'boolean' ? refresh : Boolean(refresh?.detail);
    if (doRefresh) {
      this.load();
      this.showAddForm = false;
      this.isEditMode = false;
      this.selectedMedicationId = '';
    }
  }

  // ---------- create / update / delete ----------
  public addMedication(): void {
    // Validierung und Bereinigung VOR dem Senden
    this.selectedMedication = this.ensureMedicationStructure(this.selectedMedication);

    // KRITISCH: Bereinige das Objekt vor dem Senden
    const cleanedMedication = this.cleanMedicationForServer(this.selectedMedication);

    console.log('Adding medication:', JSON.stringify(cleanedMedication, null, 2));

    this.dataService.addMedication(cleanedMedication).subscribe({
      next: (savedMedication) => {
        console.log('Medikament hinzugefügt:', savedMedication);
        this.load();
        this.showAddForm = false;
        this.isEditMode = false;
        this.selectedMedication = this.createEmptyMedication();
      },
      error: (error) => {
        console.error('Error adding medication:', error);
        this.errorMessage = String(error?.error?.message ?? error?.message ?? 'Fehler beim Hinzufügen des Medikaments');
        alert(this.errorMessage);
      }
    });
  }

  public saveMedication(): void {
    if (this.isEditMode) {
      this.updateMedication();
    } else {
      this.addMedication();
    }
  }

  public updateMedication(): void {
    if (!this.selectedMedication?.id) {
      this.errorMessage = 'Kein Medikament zum Aktualisieren ausgewählt.';
      return;
    }

    // Validierung und Bereinigung VOR dem Senden
    this.selectedMedication = this.ensureMedicationStructure(this.selectedMedication);

    // KRITISCH: Bereinige das Objekt vor dem Senden
    const cleanedMedication = this.cleanMedicationForServer(this.selectedMedication);

    this.dataService.updateMedication(this.selectedMedication.id, cleanedMedication).subscribe({
      next: (updatedMedication) => {
        console.log('Medikament aktualisiert:', updatedMedication);
        this.load();
        this.showAddForm = false;
        this.isEditMode = false;
        this.selectedMedication = this.createEmptyMedication();
      },
      error: (error) => {
        console.error('Error updating medication:', error);
        this.errorMessage = String(error?.error?.message ?? error?.message ?? 'Fehler beim Aktualisieren des Medikaments');
        alert(this.errorMessage);
      }
    });
  }

  public deleteCurrentMedication(): void {
    if (!this.selectedMedication || !this.selectedMedication.id || this.selectedMedication.id.trim() === '') {
      this.errorMessage = 'Kein Medikament zum Löschen ausgewählt';
      return;
    }

    this.deleteMedication(this.selectedMedication.id);
  }

  public clearAllMedications(): void {
    if (this.medications.length === 0) {
      this.errorMessage = 'Keine Medikamente zum Löschen vorhanden';
      return;
    }

    if (!confirm('Wirklich ALLE Medikamente löschen?')) return;
    if (!confirm('Sind Sie sich ABSOLUT sicher?')) return;

    const validMedications = this.medications.filter(med => med.id && med.id.trim() !== '');
    if (validMedications.length === 0) {
      this.errorMessage = 'Keine Medikamente mit gültigen IDs gefunden';
      return;
    }

    let deleteCount = 0;
    let errorCount = 0;

    validMedications.forEach(med => {
      this.dataService.deleteMedication(med.id!).subscribe({
        next: () => {
          deleteCount++;
          if (deleteCount + errorCount === validMedications.length) {
            this.load();
            this.selectedMedication = this.createEmptyMedication();
            this.showAddForm = false;
            this.isEditMode = false;
            this.errorMessage = `${deleteCount} Medikamente gelöscht`;
          }
        },
        error: () => {
          errorCount++;
          if (deleteCount + errorCount === validMedications.length) {
            this.errorMessage = `Fehler beim Löschen von ${errorCount} Medikamenten`;
            this.load();
          }
        }
      });
    });
  }

  public deleteMedication(id: string): void {
    if (!id) return;
    if (!confirm('Dieses Medikament wirklich löschen?')) return;

    this.dataService.deleteMedication(id).subscribe({
      next: () => {
        this.load();
        if (this.selectedMedication.id === id) {
          this.selectedMedication = this.createEmptyMedication();
          this.showAddForm = false;
          this.isEditMode = false;
        }
      },
      error: (error) => {
        this.errorMessage = String(error?.message ?? 'Fehler beim Löschen des Medikaments');
      }
    });
  }

  // ---------- selection and edit ----------
  public selectMedication(medication: Medication): void {
    this.selectedMedicationId = medication.id || '';
    this.showAddForm = false;
    this.isEditMode = false;
    this.errorMessage = '';
  }

  public cancelEdit(): void {
    this.showAddForm = false;
    this.isEditMode = false;
    this.selectedMedication = this.createEmptyMedication();
    this.selectedMedicationId = '';
    this.errorMessage = '';
  }

  public toggleAddForm(): void {
    if (!this.showAddForm) {
      this.selectedMedication = this.createEmptyMedication();
      this.isEditMode = false;
      this.selectedMedicationId = '';
    }
    this.showAddForm = !this.showAddForm;
  }

  // ---------- status helpers ----------
  public getStatusColor(status?: string | MedicationStatus | null): string {
    const s = (status ?? 'active') as string;
    switch (s.toLowerCase()) {
      case 'active': return 'success';
      case 'inactive': return 'secondary';
      case 'entered_in_error':
      case 'entered-in-error': return 'danger';
      default: return 'warning';
    }
  }

  public getStatusLabel(status?: string | MedicationStatus | null): string {
    const s = (status ?? 'active') as string;
    switch (s.toLowerCase()) {
      case 'active': return 'Aktiv';
      case 'inactive': return 'Inaktiv';
      case 'entered_in_error':
      case 'entered-in-error': return 'Fehlerhaft';
      default: return 'Unbekannt';
    }
  }
}
