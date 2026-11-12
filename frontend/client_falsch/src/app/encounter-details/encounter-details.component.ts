import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Encounter } from '../models/Encounter';
import { DataService } from '../data.service';

@Component({
  selector: 'app-encounter-details',
  standalone: true,  // <- hier wird die Komponente standalone
  imports: [CommonModule, FormsModule],
  templateUrl: './encounter-details.component.html',
  styleUrls: ['./encounter-details.component.scss']
})
export class EncounterDetailsComponent {
  @Input() encounter: Encounter = new Encounter();
  @Output() saved = new EventEmitter<void>();
  @Output() canceled = new EventEmitter<void>();

  constructor(private data: DataService) {}

  ngOnInit(): void {
    if (!this.encounter.subject) this.encounter.subject = { reference: '' } as any;
    if (!this.encounter.participant || this.encounter.participant.length === 0) {
      this.encounter.participant = [{
        type: [],
        period: { start: new Date(), end: new Date() },
        actor: { reference: '' }
      }];
    }
    const p0 = this.encounter.participant[0];
    if (p0) {
      if (!p0.period) p0.period = { start: new Date(), end: new Date() };
      if (!p0.actor) p0.actor = { reference: '' };
    }
  }

  get isValid(): boolean {
    return !!(this.encounter.status && this.encounter.subject && this.encounter.participant.length > 0);
  }

  get subjectReference(): string {
    return this.encounter.subject?.reference || '';
  }

  set subjectReference(value: string) {
    if (!this.encounter.subject) this.encounter.subject = { reference: '' } as any;
    this.encounter.subject!.reference = value;
  }

  get participantPeriodStart(): string {
    const participant = this.encounter.participant?.[0];
    return participant?.period?.start?.toString() || '';
  }

  set participantPeriodStart(value: string) {
    this.ensureParticipant();
    if (this.encounter.participant[0].period) {
      this.encounter.participant[0].period.start = value ? new Date(value) : new Date();
    }
  }

  get participantPeriodEnd(): string {
    const participant = this.encounter.participant?.[0];
    return participant?.period?.end?.toString() || '';
  }

  set participantPeriodEnd(value: string) {
    this.ensureParticipant();
    if (this.encounter.participant[0].period) {
      this.encounter.participant[0].period.end = value ? new Date(value) : new Date();
    }
  }

  get participantActorReference(): string {
    const participant = this.encounter.participant?.[0];
    return participant?.actor?.reference || '';
  }

  set participantActorReference(value: string) {
    this.ensureParticipant();
    if (this.encounter.participant[0].actor) {
      this.encounter.participant[0].actor.reference = value;
    }
  }

  private ensureParticipant(): void {
    if (!this.encounter.participant || this.encounter.participant.length === 0) {
      this.encounter.participant = [{
        type: [],
        period: { start: new Date(), end: new Date() },
        actor: { reference: '' }
      }];
    }
    const p0 = this.encounter.participant[0];
    if (!p0.period) p0.period = { start: new Date(), end: new Date() };
    if (!p0.actor) p0.actor = { reference: '' };
  }

  save(): void {
    if (!this.isValid) return;
    const hasId = !!this.encounter.id;
    (hasId
        ? this.data.updateEncounter(this.encounter)
        : this.data.addEncounter(this.encounter)
    ).subscribe(() => this.saved.emit());
  }

  cancel(): void {
    this.canceled.emit();
  }
}
