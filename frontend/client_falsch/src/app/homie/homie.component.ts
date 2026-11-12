import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { KeycloakService } from 'keycloak-angular';
import { DataService } from '../data.service';

@Component({
  selector: 'app-home',
  imports: [CommonModule, RouterLink],
  templateUrl: './homie.component.html',
  styleUrls: ['./homie.component.scss'],
  standalone: true
})
export class HomieComponent implements OnInit {

  patients: any[] = [];
  loading = false;
  error: string | null = null;
  hasToken: boolean = false;

  constructor(
    public keycloakService: KeycloakService,
    private dataService: DataService
  ) {}

  async ngOnInit(): Promise<void> {
    console.log('HomieComponent initialized');

    if (this.keycloakService.isLoggedIn()) {
      // Token-Status prüfen
      try {
        const token = await this.keycloakService.getToken();
        this.hasToken = !!token;
      } catch (err) {
        this.hasToken = false;
      }

      await this.loadPatients();
    }
  }

  async loadPatients(): Promise<void> {
    this.loading = true;
    this.error = null;

    try {
      this.dataService.getPatients().subscribe({
        next: (data) => {
          this.patients = data;
          this.loading = false;
        },
        error: (err) => {
          console.error('Fehler beim Laden der Patienten:', err);
          this.error = 'Fehler beim Laden der Patienten: ' + err.message;
          this.loading = false;
        }
      });
    } catch (err: any) {
      this.error = 'Fehler beim Laden der Patienten.';
      this.loading = false;
    }
  }
}
