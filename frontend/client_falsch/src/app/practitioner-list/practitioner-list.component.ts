import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { DataService } from '../data.service';
import { Practitioner } from '../models/Practitioner';
import { PractitionerDetailsComponent } from '../practitioner-details/practitioner-details.component';

@Component({
  selector: 'app-practitioner-list',
  imports: [CommonModule, FormsModule, PractitionerDetailsComponent],
  templateUrl: './practitioner-list.component.html',
  standalone: true,
  styleUrls: ['./practitioner-list.component.scss']
})
export class PractitionerListComponent implements OnInit {
  constructor(private dataService: DataService) {}

  public practitioners: Practitioner[] = [];
  public selected: Practitioner | null = null;

  // Search & paging
  public searchParams: any = {};
  public page: number = 1;
  public count: number = 10;
  public total: number = 0;

  Math = Math; // Für Template-Zugriff

  ngOnInit(): void {
    this.load();
  }

  load(): void {
    const params = {...this.searchParams, _page: this.page, _count: this.count};
    this.dataService.searchPractitioners(params).subscribe(r => {
      this.practitioners = r.practitioners || [];
      this.total = r.total || 0;
    });
  }

  nextPage(): void { this.page++; this.load(); }
  prevPage(): void { if (this.page>1) { this.page--; this.load(); } }

  resetSearch(): void {
    this.searchParams = {};
    this.page = 1;
    this.load();
  }

  selectPractitioner(row: Practitioner): void {
    // Klonen, damit Abbruch im Formular die Liste nicht „live“ verändert
    this.selected = JSON.parse(JSON.stringify(row));
  }

  addPractitioner(): void {
    this.selected = new Practitioner();
    // Pflichtfelder initialisieren nach Backend-Entity
    this.selected.active = true;
    this.selected.name = [{
      use: 'official',
      text: '',
      family: '',
      given: [''],
      prefix: [''],
      suffix: ['']
    }];
    this.selected.gender = 'female'; // oder 'male', kann im Formular geändert werden
    this.selected.birthDate = new Date('2025-09-15'); // aktuelles Datum als Date-Objekt
    this.selected.address = [{
      use: 'home',
      type: 'both',
      text: '',
      line: [''],
      city: '',
      district: '',
      state: '',
      postalCode: '', // wie im Backend-Entity
      country: ''
    }];
    this.selected.telecom = [{ system: 'phone', value: '', use: 'home', rank: 0 }];
    this.selected.attachment = [];
    this.selected.identifier = [];
    this.selected.qualification = [];
    this.selected.communication = [];
  }

  onSavedOrCanceled(refresh: boolean): void {
    if (refresh) this.load();
    this.selected = null;
  }

  trackById = (_: number, p: Practitioner) => p.id ||_;
}
