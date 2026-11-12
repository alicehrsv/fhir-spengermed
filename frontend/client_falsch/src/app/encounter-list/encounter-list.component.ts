import { Component, OnInit } from '@angular/core';
import { Encounter } from '../models/Encounter';
import { DataService } from '../data.service';
import { EncounterDetailsComponent } from '../encounter-details/encounter-details.component';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-encounter-list',
  standalone: true, // <- wichtig
  imports: [CommonModule, FormsModule, EncounterDetailsComponent],
  templateUrl: './encounter-list.component.html',
  styleUrls: ['./encounter-list.component.scss']
})
export class EncounterListComponent implements OnInit {
  public encounters: Encounter[] = [];
  public selected: Encounter | null = null;

  constructor(private data: DataService) {}

  ngOnInit(): void {
    this.load();
  }

  load(): void {
    this.data.getEncounters().subscribe(e => this.encounters = e || []);
  }

  selectEncounter(enc: Encounter): void {
    this.selected = JSON.parse(JSON.stringify(enc));
  }

  addEncounter(): void {
    this.selected = new Encounter();
  }

  onSavedOrCanceled(refresh: boolean): void {
    if (refresh) this.load();
    this.selected = null;
  }
}
