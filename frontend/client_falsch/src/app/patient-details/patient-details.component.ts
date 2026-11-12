import { Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges } from '@angular/core';
import { DataService } from '../data.service';
import { CommonModule } from '@angular/common';
import { Patient } from '../models/Patient';
import { FormsModule } from '@angular/forms';
import { Gender } from '../models/Patient';
import { HumanName } from '../models/HumanName';
import { Address } from '../models/Address';
@Component({
  selector: 'app-patient-details',
  imports: [CommonModule, FormsModule],
  templateUrl: './patient-details.component.html',
  standalone: true,
  styleUrl: './patient-details.component.scss'
})
export class PatientDetailsComponent implements OnInit, OnChanges {

  @Input() id: string = '';
  @Output() patientModified = new EventEmitter<boolean>();

  ngOnChanges(): void {
    this.getPatientDetails()
  }

  constructor(private dataService: DataService) { }
  ngOnInit(): void {
    this.getPatientDetails();
  }
  public patient: Patient = new Patient('');
  public genders = Object.values(Gender)

  // Removed duplicate getPatientDetails method


  public addGiven(name: HumanName) {

    name.given.push('');
    console.log(name.given);

  }


  public deletePatient() {

    this.dataService.deletePatient(this.patient.id)
      .subscribe()
    this.patientModified.emit(true);
    ;

  }




  public addAddressLine(address: Address) {
    address.line.push('');
  }

  public savePatient(): void {
    // DTO für Backend erzeugen
    const patientDto = {
      id: this.patient.id, // wichtig: gleiche ID
      name: this.patient.name.map(n => ({
        family: n.family || '',
        given: n.given || ['']
      })),
      gender: this.patient.gender,
      birthDate: this.patient.birthDate ? this.formatDate(this.patient.birthDate) : '',
      adresses: this.patient.adresses.map(a => ({ line: a.line || [''] })),
      telecom: this.patient.telecom.map(t => ({
        system: t.system || 'phone',
        value: t.value || '',
        use: t.use || 'home',
        rank: t.rank || 0
      }))
    };

    this.dataService.savePatient(patientDto as any)
      .subscribe({
        next: (p) => {
          console.log('Patient aktualisiert:', p);
          // Formular schließen: Parent weiß, dass Patient geändert wurde
          this.patientModified.emit(true);
        },
        error: (err) => console.error('Fehler beim Speichern:', err)
      });
  }

  private formatDate(date: Date | string): string {
    if (!date) return '';
    if (typeof date === 'string') return date;
    return date.toISOString().split('T')[0]; // yyyy-MM-dd
  }

  private ensurePatientArrays(): void {
    // Name
    if (!this.patient.name || this.patient.name.length === 0) {
      this.patient.name = [new HumanName()];
    } else {
      this.patient.name.forEach(n => {
        if (!n.given || n.given.length === 0) n.given = [''];
        if (!n.prefix) n.prefix = [];
        if (!n.suffix) n.suffix = [];
      });
    }

    // Adresse
    if (!this.patient.adresses || this.patient.adresses.length === 0) {
      this.patient.adresses = [{
        line: [''],
        use: '',
        type: '',
        text: '',
        city: '',
        district: '',
        state: '',
        postalCode: '',
        country: ''
      }];
    } else {
      this.patient.adresses.forEach(a => {
        if (!a.line || a.line.length === 0) a.line = [''];
      });
    }

    // Kontakt/Telecom
    if (!this.patient.telecom || this.patient.telecom.length === 0) {
      this.patient.telecom = [{ system: 'phone', value: '', use: 'home', rank: 0 }];
    }
  }
  public getPatientDetails(): void {
    this.dataService.getPatient(this.id)
      .subscribe(patient => {
        console.log(patient);
        this.patient = patient;

        // Sicherstellen, dass Arrays existieren
        this.ensurePatientArrays();
      });
  }


}
