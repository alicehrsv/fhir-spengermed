import { Component, EventEmitter, Input, OnChanges, OnInit, Output, TrackByFunction } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { DataService } from '../data.service';

import { Practitioner } from '../models/Practitioner';
import { HumanName } from '../models/HumanName';
import { Address } from '../models/Address';
import { ContactPoint } from '../models/ContactPoint';
import { Atachment } from '../models/Atachment';
import { Qualification } from '../models/Qualification';
import { Gender } from '../models/Patient';

@Component({
  selector: 'app-practitioner-details',
  imports: [CommonModule, FormsModule],
  templateUrl: './practitioner-details.component.html',
  standalone: true,
  styleUrls: ['./practitioner-details.component.scss']
})
export class PractitionerDetailsComponent implements OnInit, OnChanges {

  @Input() practitioner: Practitioner = new Practitioner('');
  @Output() saved = new EventEmitter<void>();
  @Output() canceled = new EventEmitter<void>();

  genders: string[] = Object.values(Gender);

  constructor(private data: DataService) { }

  ngOnInit(): void {
    this.ensureInit();
    // Falls Bearbeitungsmodus (mit id) -> frisch laden
    if (this.practitioner?.id) this.loadById(this.practitioner.id);
  }

  ngOnChanges(): void {
    this.ensureInit();
    if (this.practitioner?.id) this.loadById(this.practitioner.id);
  }

  private loadById(id: string): void {
    if (!id) return;
    this.data.getPractitioners(id).subscribe(p => {
      const got = Array.isArray(p) ? p[0] : p;
      if (got) this.practitioner = this.hydrate(got);
    });
  }

  // Stellt sicher, dass Arrays existieren (für ngFor und ngModel)
  private ensureInit(): void {
    if (!this.practitioner) this.practitioner = new Practitioner('');
    this.practitioner.name = this.practitioner.name?.length ? this.practitioner.name : [new HumanName()];
    this.practitioner.address = this.practitioner.address || [];
    this.practitioner.telecom = this.practitioner.telecom || [];
    this.practitioner.photo = this.practitioner.photo || [];
    this.practitioner.qualification = this.practitioner.qualification || [];
    this.practitioner.communication = this.practitioner.communication || [];
  }

  private hydrate(p: Practitioner): Practitioner {
    // gleiche Initialisierung wie ensureInit – aber auf vorhandenen Daten
    p.name = p.name?.length ? p.name : [new HumanName()];
    p.address = p.address || [];
    p.telecom = p.telecom || [];
    p.photo = p.photo || [];
    p.qualification = p.qualification || [];
    p.communication = p.communication || [];
    return p;
  }

  // Buttons / Helpers
  trackByIndex: TrackByFunction<any> = (i: number) => i;

  addName(): void {
    this.practitioner.name.push(new HumanName());
  }

  addGiven(n: HumanName): void {
    if (!n.given) n.given = [];
    n.given.push('');
  }
  addAdresse(): void {
    this.practitioner.address.push(new Address('', '', '', [], '', '', '', '', ''));
  }


  addLine(a: Address): void {
    if (!a.line) a.line = [];
    a.line.push('');
  }

  addTelecom(): void {
    this.practitioner.telecom.push(new ContactPoint('phone', '', '', 0));
  }

  addPhoto(): void {
    this.practitioner.photo.push(new Atachment('image/jpeg', '', '', '', 0));
  }

  addQualification(): void {
    const q = new Qualification();
    (q as any).identifier = (q as any).identifier || [{ system: '', value: '' }];
    this.practitioner.qualification.push(q);
  }

  addQualificationIdentifier(q: any): void {
    q.identifier = q.identifier || [];
    q.identifier.push({ system: '', value: '' });
  }

  addCommunication(): void {
    // FHIR CodeableConcept minimal
    (this.practitioner as any).communication.push({ text: '' });
  }

  // Validierung (Minimal: Name, Geschlecht, Geburtstag)
  get isValid(): boolean {
    const n = this.practitioner.name?.[0];
    const hasName = !!(n && ((n.given && n.given[0] && n.given[0].trim()) || (n.family && n.family.trim())));
    const hasGender = !!this.practitioner.gender;
    const hasBirth = !!this.practitioner.birthDate;
    return hasName && hasGender && hasBirth;
  }

  save(): void {
    if (!this.isValid) return;

    // Fix: ensure address and telecom arrays are not empty and have valid objects
    if (!this.practitioner.address || this.practitioner.address.length === 0) {
      this.practitioner.address = [new Address('', '', '', [''], '', '', '', '', '')];
    } else {
      this.practitioner.address.forEach(a => {
        if (!a.line || a.line.length === 0) a.line = [''];
        if ((a as any).postalcode === undefined) (a as any).postalcode = '';
        if (a.city === undefined) a.city = '';
        if (a.state === undefined) a.state = '';
        if (a.country === undefined) a.country = '';
      });
    }
    if (!this.practitioner.telecom || this.practitioner.telecom.length === 0) {
      this.practitioner.telecom = [new ContactPoint('phone', '', '', 0)];
    } else {
      this.practitioner.telecom.forEach(t => {
        if (t.system === undefined) t.system = 'phone';
        if (t.value === undefined) t.value = '';
        if (t.use === undefined) t.use = 'home';
        if (t.rank === undefined) t.rank = 0;
      });
    }

    // Log payload for debugging
    console.log('Payload:', this.practitioner);

    const hasId = !!this.practitioner.id;

    (hasId
        ? this.data.updatePractitioner(this.practitioner)
        : this.data.addPractitioner(this.practitioner)
    ).subscribe(saved => {
      this.practitioner = this.hydrate(saved);
      this.saved.emit();
    });
  }

  delete(): void {
    if (!this.practitioner.id) return;
    this.data.deletePractitioner(this.practitioner.id).subscribe(() => {
      this.canceled.emit(); // nach Löschen schließen/refresh
    });
  }

  cancel(): void {
    this.canceled.emit();
  }
}
