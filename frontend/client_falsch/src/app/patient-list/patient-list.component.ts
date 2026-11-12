import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { DataService } from '../data.service';
import { Patient, Gender } from '../models/Patient';
import { PatientDetailsComponent } from '../patient-details/patient-details.component';
import { HumanName } from '../models/HumanName';
import { FormsModule } from '@angular/forms';
import { Address } from '../models/Address';
import { ContactPoint } from '../models/ContactPoint';
import { Bundle, BundleEntry } from '../models/Bundle';

@Component({
  selector: 'app-patient-list',
  imports: [CommonModule, FormsModule, PatientDetailsComponent],
  templateUrl: './patient-list.component.html',
  standalone: true,
  styleUrl: './patient-list.component.scss'
})
export class PatientListComponent implements OnInit {

  constructor(private dataService: DataService) { }

  public patients: Patient[] = [];
  public selectedPatient: Patient = this.createEmptyPatient();
  public showAddForm = false;
  public genders = Object.values(Gender);

  ngOnInit() {
    this.searchPatients();
  }

  getPatients(): void {
    this.dataService.getPatients()
      .subscribe(patients => {
        this.patients = patients;
      });
  }

  public selectPatient(patient: Patient): void {
    this.selectedPatient = patient;
  }

  public onpatientModified(hideDetails: boolean): void {
    this.getPatients();
    if (hideDetails) {
      this.selectedPatient = new Patient('');
    }
  }

  public addPatient(): void {
    // Sauberes DTO für Backend
    const patientDto = {
      name: [{
        family: this.selectedPatient.name[0].family || '',
        given: this.selectedPatient.name[0].given || ['']
      }],
      gender: this.selectedPatient.gender,
      birthDate: this.selectedPatient.birthDate ? this.formatDate(this.selectedPatient.birthDate) : '',
      adresses: [{
        line: this.selectedPatient.adresses[0].line || ['']
      }],
      telecom: [{
        system: this.selectedPatient.telecom[0].system || 'phone',
        value: this.selectedPatient.telecom[0].value || '',
        use: this.selectedPatient.telecom[0].use || 'home',
        rank: this.selectedPatient.telecom[0].rank || 0
      }]
    };

    this.dataService.addPatient(patientDto as any) // DTO casten
      .subscribe({
        next: (patient) => {
          console.log("Patient gespeichert:", patient);
          this.getPatients();      // Tabelle aktualisieren
          this.showAddForm = false;
          this.selectedPatient = this.createEmptyPatient();
        },
        error: (err) => console.error("Fehler beim Speichern:", err)
      });
  }

// Hilfsfunktion: Datum formatieren für Backend
  private formatDate(date: Date | string): string {
    if (!date) return '';
    if (typeof date === 'string') return date;
    return date.toISOString().split('T')[0]; // yyyy-MM-dd
  }


  public toggleAddForm(): void {
    if (!this.showAddForm) {
      this.selectedPatient = this.createEmptyPatient();
    }
    this.showAddForm = !this.showAddForm;
  }

  private createEmptyPatient(): Patient {
    return new Patient(
      '',
      [new HumanName('', '', '', [''])],
      Gender.UNKNOWN,
      false,
      new Date(),
      [new Address('')],
      [],
      new Date(),
      false,
      undefined,
      [new ContactPoint('phone', '', 'home', 0)]
    );
  }
  searchParams = {
    family: '',
    given: '',
    gender: '',
    address: '',
    birthdate: '',
    telecom: '',
    _count: 10,
    _page: 1
  };

  total = 0;
  Math = Math; // Make Math available in template

  searchPatients() {
    // Filter leere Parameter raus
    const cleanedParams: any = {};
    Object.keys(this.searchParams).forEach(key => {
      const value = (this.searchParams as any)[key];
      if (value !== '' && value !== null && value !== undefined) {
        cleanedParams[key] = value;
      }
    });

    this.dataService.searchPatients(cleanedParams)
      .subscribe({
        next: (bundle) => {
          this.patients = bundle.entry ? bundle.entry.map((e: BundleEntry) => e.resource) : [];
          this.total = bundle.total || 0;
        },
        error: (err) => {
          console.error('Fehler bei der Suche:', err);
          this.patients = [];
          this.total = 0;
        }
      });
  }

  nextPage() {
    if (this.searchParams._page * this.searchParams._count < this.total) {
      this.searchParams._page++;
      this.searchPatients();
    }
  }

  prevPage() {
    if (this.searchParams._page > 1) {
      this.searchParams._page--;
      this.searchPatients();
    }
  }

  resetSearch() {
    this.searchParams = {
      family: '',
      given: '',
      gender: '',
      address: '',
      birthdate: '',
      telecom: '',
      _count: 10,
      _page: 1
    };
    this.searchPatients();
  }

  setPageSize(size: number) {
    this.searchParams._count = size;
    this.searchParams._page = 1; // Reset to first page
    this.searchPatients();
  }

}
