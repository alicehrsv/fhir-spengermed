import { Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { DataService } from '../data.service';
import { Medication, MedicationStatus } from '../models/Medication';
import { CodeableConcept } from '../models/CodeableConcept';
import { Ratio } from '../models/Ratio';
import { MedicationIngredient } from '../models/MedicationIngredient';
import { MedicationBatch } from '../models/MedicationBatch';
import { Identifier } from '../models/Identifier';
import { Reference } from '../models/Reference';
import { Quantity } from '../models/Quantity';
import { Coding } from '../models/Coding';

@Component({
  selector: 'app-medication-details',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './medication-details.component.html',
  styleUrls: ['./medication-details.component.scss']
})
export class MedicationDetailsComponent implements OnInit, OnChanges {

  @Input() id?: string = '';
  @Input() medication: Medication = new Medication();
  @Output() medicationModified = new EventEmitter<boolean>();

  medicationStatuses: string[] = Object.values(MedicationStatus);
  public isLoading = false;
  public errorMessage = '';

  constructor(private dataService: DataService) { }

  ngOnInit(): void {
    this.ensureMedicationStructures();
    if (this.id) {
      this.getMedicationDetails();
    }
  }

  ngOnChanges(_changes?: SimpleChanges): void {
    this.ensureMedicationStructures();
    if (this.id) {
      this.getMedicationDetails();
    }
  }

  // ---------- Sicherstellen, dass alle Objekte/Arrays existieren ----------
  private ensureMedicationStructures(): void {
    if (!this.medication) {
      this.medication = new Medication();
    }

    // ensure status default
    if (!this.medication.status) {
      this.medication.status = MedicationStatus.ACTIVE as unknown as any;
    }

    // Code: immer CodeableConcept setzen mit gültigem coding
    this.medication.code = this.medication.code ?? new CodeableConcept();
    this.medication.code.text = this.medication.code.text ?? '';
    this.medication.code.coding = this.medication.code.coding ?? [{
      system: 'http://www.nlm.nih.gov/research/umls/rxnorm',
      code: '',
      display: ''
    }];

    // Form: immer CodeableConcept setzen mit gültigem coding
    this.medication.form = this.medication.form ?? new CodeableConcept();
    this.medication.form.text = this.medication.form.text ?? '';
    this.medication.form.coding = this.medication.form.coding ?? [{
      system: 'http://snomed.info/sct',
      code: '',
      display: ''
    }];

    // Amount: ALLE Felder mit gültigen Standardwerten initialisieren
    this.medication.amount = this.medication.amount ?? new Ratio();
    this.medication.amount.numerator = this.medication.amount.numerator ?? new Quantity();

    // Numerator mit Standardwerten
    if (this.medication.amount.numerator.value === null || this.medication.amount.numerator.value === undefined) {
      this.medication.amount.numerator.value = 1;
    }
    this.medication.amount.numerator.unit = this.medication.amount.numerator.unit || 'mg';
    this.medication.amount.numerator.system = this.medication.amount.numerator.system || 'http://unitsofmeasure.org';
    this.medication.amount.numerator.code = this.medication.amount.numerator.code || 'mg';

    this.medication.amount.denominator = this.medication.amount.denominator ?? new Quantity();

    // Denominator mit Standardwerten
    if (this.medication.amount.denominator.value === null || this.medication.amount.denominator.value === undefined) {
      this.medication.amount.denominator.value = 1;
    }
    this.medication.amount.denominator.unit = this.medication.amount.denominator.unit || 'tablet';
    this.medication.amount.denominator.system = this.medication.amount.denominator.system || 'http://unitsofmeasure.org';
    this.medication.amount.denominator.code = this.medication.amount.denominator.code || 'tablet';

    // Batch: ensure object + gültiges lotNumber Array
    this.medication.batch = this.medication.batch ?? new MedicationBatch();
    if (!this.medication.batch.lotNumber || this.medication.batch.lotNumber.length === 0) {
      this.medication.batch.lotNumber = [''];
    }
    this.medication.batch.expirationDate = this.medication.batch.expirationDate ?? '';

    // Manufacturer: Vollständig mit reference und display
    this.medication.manufacturer = this.medication.manufacturer ?? new Reference();
    this.medication.manufacturer.reference = this.medication.manufacturer.reference ?? '';
    this.medication.manufacturer.display = this.medication.manufacturer.display ?? '';

    // Arrays initialisieren
    this.medication.identifier = this.medication.identifier ?? [];
    this.medication.ingredient = this.medication.ingredient ?? [];

    // ID und Status
    this.medication.id = this.medication.id ?? '';
    this.medication.status = this.medication.status ?? (MedicationStatus.ACTIVE as unknown as any);
  }

  public getMedicationDetails(): void {
    if (!this.id) return;
    this.dataService.getMedication(this.id!).subscribe({
      next: (medication) => {
        this.medication = medication;
        this.ensureMedicationStructures();
      },
      error: (error) => {
        console.error('Error loading medication:', error);
      }
    });
  }

  // prepareForSave füllt automatisch coding/lotNumber/etc. sinnvoll auf
  private prepareForSave(): void {
    console.log('prepareForSave: START - Current medication:', JSON.stringify(this.medication, null, 2));
    this.ensureMedicationStructures();

    // 1. Code: Sicherstellen, dass coding-Array existiert und gültig ist
    if (this.medication.code) {
      this.medication.code.coding = this.medication.code.coding ?? [];
      if (this.medication.code.coding.length === 0 || !this.medication.code.coding[0].code) {
        this.medication.code.coding = [{
          system: 'http://www.nlm.nih.gov/research/umls/rxnorm',
          code: this.medication.code.text ?? 'unknown',
          display: this.medication.code.text ?? 'unknown'
        }];
      }
    }

    // 2. Form: Sicherstellen, dass coding-Array existiert und gültig ist
    if (this.medication.form) {
      this.medication.form.coding = this.medication.form.coding ?? [];
      if (this.medication.form.coding.length === 0 || !this.medication.form.coding[0].code) {
        this.medication.form.coding = [{
          system: 'http://snomed.info/sct',
          code: this.medication.form.text ?? 'unknown',
          display: this.medication.form.text ?? 'unknown'
        }];
      }
    }

    // 3. Manufacturer: Reference muss einen gültigen Wert haben
    if (this.medication.manufacturer) {
      if (!this.medication.manufacturer.reference || this.medication.manufacturer.reference.trim() === '') {
        const display = this.medication.manufacturer.display || 'Unknown Manufacturer';
        this.medication.manufacturer.reference = `Organization/${display.replace(/\s+/g, '-').toLowerCase()}`;
        console.log('prepareForSave: Generated manufacturer reference:', this.medication.manufacturer.reference);
      }
    }

    // 4. Amount: ALLE null-Werte ersetzen - KRITISCH!
    if (this.medication.amount) {
      console.log('prepareForSave: Processing amount - before:', JSON.stringify(this.medication.amount, null, 2));

      // Numerator komplett neu setzen falls Probleme
      if (!this.medication.amount.numerator || this.medication.amount.numerator.value === null) {
        this.medication.amount.numerator = {
          value: 1,
          unit: 'mg',
          system: 'http://unitsofmeasure.org',
          code: 'mg'
        } as Quantity;
      } else {
        // Einzelne Felder korrigieren
        if (this.medication.amount.numerator.value === null || this.medication.amount.numerator.value === undefined) {
          this.medication.amount.numerator.value = 1;
        }
        this.medication.amount.numerator.unit = this.medication.amount.numerator.unit || 'mg';
        this.medication.amount.numerator.system = this.medication.amount.numerator.system || 'http://unitsofmeasure.org';
        this.medication.amount.numerator.code = this.medication.amount.numerator.code || 'mg';
      }

      // Denominator komplett neu setzen falls Probleme
      if (!this.medication.amount.denominator || this.medication.amount.denominator.value === null) {
        this.medication.amount.denominator = {
          value: 1,
          unit: 'tablet',
          system: 'http://unitsofmeasure.org',
          code: 'tablet'
        } as Quantity;
      } else {
        // Einzelne Felder korrigieren
        if (this.medication.amount.denominator.value === null || this.medication.amount.denominator.value === undefined) {
          this.medication.amount.denominator.value = 1;
        }
        this.medication.amount.denominator.unit = this.medication.amount.denominator.unit || 'tablet';
        this.medication.amount.denominator.system = this.medication.amount.denominator.system || 'http://unitsofmeasure.org';
        this.medication.amount.denominator.code = this.medication.amount.denominator.code || 'tablet';
      }

      console.log('prepareForSave: Processing amount - after:', JSON.stringify(this.medication.amount, null, 2));
    }

    // 5. Batch: lotNumber muss ein gültiges Array sein
    if (this.medication.batch) {
      if (!this.medication.batch.lotNumber || this.medication.batch.lotNumber.length === 0) {
        this.medication.batch.lotNumber = ['unknown'];
      }
      // Stelle sicher, dass keine leeren Strings im Array sind
      this.medication.batch.lotNumber = this.medication.batch.lotNumber.filter(lot => lot && lot.trim() !== '');
      if (this.medication.batch.lotNumber.length === 0) {
        this.medication.batch.lotNumber = ['unknown'];
      }
    }

    // 6. Ingredients: Vollständige Validierung
    this.medication.ingredient = this.medication.ingredient ?? [];
    this.medication.ingredient.forEach(ing => {
      // ItemCodeableConcept
      ing.itemCodeableConcept = ing.itemCodeableConcept ?? new CodeableConcept();
      ing.itemCodeableConcept.coding = ing.itemCodeableConcept.coding ?? [];
      if (ing.itemCodeableConcept.coding.length === 0) {
        ing.itemCodeableConcept.coding = [{
          system: 'http://www.nlm.nih.gov/research/umls/rxnorm',
          code: ing.itemCodeableConcept.text ?? 'unknown',
          display: ing.itemCodeableConcept.text ?? 'unknown'
        }];
      }

      // Strength (Ratio)
      ing.strength = ing.strength ?? new Ratio();
      ing.strength.numerator = ing.strength.numerator ?? new Quantity();
      ing.strength.denominator = ing.strength.denominator ?? new Quantity();

      if (ing.strength.numerator.value === null || ing.strength.numerator.value === undefined) {
        ing.strength.numerator.value = 1;
      }
      if (ing.strength.denominator.value === null || ing.strength.denominator.value === undefined) {
        ing.strength.denominator.value = 1;
      }

      ing.strength.numerator.unit = ing.strength.numerator.unit || 'mg';
      ing.strength.numerator.system = ing.strength.numerator.system || 'http://unitsofmeasure.org';
      ing.strength.numerator.code = ing.strength.numerator.code || 'mg';

      ing.strength.denominator.unit = ing.strength.denominator.unit || 'tablet';
      ing.strength.denominator.system = ing.strength.denominator.system || 'http://unitsofmeasure.org';
      ing.strength.denominator.code = ing.strength.denominator.code || 'tablet';
    });

    // 7. Entferne leere oder ungültige Felder
    if (this.medication.identifier) {
      this.medication.identifier = this.medication.identifier.filter(id =>
        id.system && id.system.trim() !== '' && id.value && id.value.trim() !== ''
      );
    }

    console.log('prepareForSave: FINAL RESULT:', JSON.stringify(this.medication, null, 2));
  }

  public saveMedication(): void {
    // Ensure fields are normalized before sending
    this.prepareForSave();
    console.log('SAVE MEDICATION: START - Original payload:', JSON.stringify(this.medication, null, 2));

    // Ensure mandatory status
    if (!this.medication.status) {
      this.medication.status = MedicationStatus.ACTIVE as unknown as any;
    }

    // 1. CODE: Coding Array erstellen
    if (this.medication.code) {
      this.medication.code.coding = [{
        system: 'http://www.nlm.nih.gov/research/umls/rxnorm',
        code: this.medication.code.text || 'unknown',
        display: this.medication.code.text || 'unknown'
      }];
    }

    // 2. FORM: Coding Array erstellen
    if (this.medication.form) {
      this.medication.form.coding = [{
        system: 'http://snomed.info/sct',
        code: this.medication.form.text || 'unknown',
        display: this.medication.form.text || 'unknown'
      }];
    }

    // 3. MANUFACTURER: Reference erstellen
    if (this.medication.manufacturer) {
      if (!this.medication.manufacturer.reference || this.medication.manufacturer.reference.trim() === '') {
        const display = this.medication.manufacturer.display || 'unknown';
        this.medication.manufacturer.reference = `Organization/${display.toLowerCase().replace(/\s+/g, '-').replace(/[^a-z0-9-]/g, '')}`;
      }
    }

    // 4. AMOUNT: KOMPLETT NEU ERSTELLEN (prepareForSave already set sensible defaults)
    this.medication.amount = {
      numerator: {
        value: this.medication.amount?.numerator?.value || 1,
        unit: this.medication.amount?.numerator?.unit || 'mg',
        system: 'http://unitsofmeasure.org',
        code: 'mg'
      },
      denominator: {
        value: this.medication.amount?.denominator?.value || 1,
        unit: this.medication.amount?.denominator?.unit || 'tablet',
        system: 'http://unitsofmeasure.org',
        code: 'tablet'
      }
    } as Ratio;

    // 5. BATCH: LotNumber bereinigen
    if (this.medication.batch && this.medication.batch.lotNumber) {
      this.medication.batch.lotNumber = this.medication.batch.lotNumber.filter(lot => lot && lot.trim() !== '');
      if (this.medication.batch.lotNumber.length === 0) {
        this.medication.batch.lotNumber = ['unknown'];
      }
    }

    // 6. Leere Identifier entfernen
    if (this.medication.identifier) {
      this.medication.identifier = this.medication.identifier.filter(id =>
        id.system && id.system.trim() !== '' && id.value && id.value.trim() !== ''
      );
    }

    console.log('SAVE MEDICATION: CLEANED payload:', JSON.stringify(this.medication, null, 2));

    const isUpdate = !!this.medication.id;
    const operation = isUpdate
      ? this.dataService.updateMedication(this.medication.id, this.medication)
      : this.dataService.addMedication(this.medication);

    operation.subscribe({
      next: (savedMedication) => {
        console.log(`Medication ${isUpdate ? 'updated' : 'created'}:`, savedMedication);
        if (savedMedication && savedMedication.id) {
          this.medication.id = savedMedication.id;
        }
        this.medicationModified.emit(true);
      },
      error: (error) => {
        console.error('Error saving medication:', error);
        alert('Fehler beim Speichern: ' + (error?.message ?? error));
      }
    });
  }

  public deleteMedication(): void {
    if (!this.medication || !this.medication.id) return;
    if (!confirm('Dieses Medikament wirklich löschen?')) return;
    this.dataService.deleteMedication(this.medication.id).subscribe({
      next: () => this.medicationModified.emit(true),
      error: (err) => {
        console.error('Error deleting medication', err);
        this.errorMessage = String(err?.message ?? 'Fehler beim Löschen');
        alert(this.errorMessage);
      }
    });
  }

  // Identifier
  public addIdentifier(): void {
    this.ensureMedicationStructures();
    this.medication!.identifier.push(new Identifier());
  }
  public removeIdentifier(index: number): void {
    if (!this.medication || !this.medication.identifier) return;
    this.medication.identifier.splice(index, 1);
  }

  // Ingredients
  public addIngredient(): void {
    this.ensureMedicationStructures();
    const ing = new MedicationIngredient();
    ing.itemCodeableConcept = new CodeableConcept();
    ing.strength = new Ratio();
    ing.itemReference = new Reference();
    this.medication!.ingredient.push(ing);
  }
  public removeIngredient(index: number): void {
    if (!this.medication || !this.medication.ingredient) return;
    this.medication.ingredient.splice(index, 1);
  }

  public cancel(): void {
    this.medicationModified.emit(false);
  }

  // ---------- Hilfs-Property für sichere Binding von lotNumber[0] ----------
  public get batchLotNumber(): string {
    if (!this.medication) return '';
    if (!this.medication.batch) {
      this.medication.batch = new MedicationBatch();
      this.medication.batch.lotNumber = [''];
    }
    return this.medication.batch.lotNumber && this.medication.batch.lotNumber.length > 0
      ? this.medication.batch.lotNumber[0] ?? ''
      : '';
  }

  public set batchLotNumber(value: string) {
    if (!this.medication) this.medication = new Medication();
    if (!this.medication.batch) this.medication.batch = new MedicationBatch();
    this.medication.batch.lotNumber = this.medication.batch.lotNumber ?? [''];
    this.medication.batch.lotNumber[0] = value ?? '';
  }

  // Simple validity check for template
  public get isValid(): boolean {
    if (!this.medication) return false;
    if (!this.medication.code || !this.medication.code.text || this.medication.code.text.trim() === '') return false;
    if (!this.medication.status) return false;
    // batch date can be optional, but ensure lotNumber has at least one non-empty
    if (!this.medication.batch || !this.medication.batch.lotNumber || this.medication.batch.lotNumber.length === 0) return false;
    return true;
  }

  // ---------- TrackBy für ngFor ----------
  public trackByIndex(index: number): number {
    return index;
  }
}
