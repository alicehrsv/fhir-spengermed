import { Component } from '@angular/core';
import {RouterLink, RouterModule} from '@angular/router';
import { CommonModule } from '@angular/common';
import { KeycloakService } from 'keycloak-angular';
import {NavbarComponent} from './navbar/navbar.component';
@Component({
    selector: 'app-root',
    imports: [RouterModule, NavbarComponent], // ← RouterModule hier hinzufügen
    templateUrl: './app.component.html',
    styleUrls: ['./app.component.scss'],
    standalone: true
})
export class AppComponent {

  constructor(public keycloakService: KeycloakService) {}

  /**
   * Abmelden
   */
  logout(): void {
    this.keycloakService.logout();
  }

  /**
   * Anmelden (falls nötig)
   */
  login(): void {
    this.keycloakService.login();
  }
}
