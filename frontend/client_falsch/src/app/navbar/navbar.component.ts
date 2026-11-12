import { Component, OnInit } from '@angular/core';
import { RouterModule } from '@angular/router';
import { KeycloakService } from 'keycloak-angular';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [RouterModule, CommonModule],
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss']  // Tippfehler 'styleUrl' → 'styleUrls'
})
export class NavbarComponent implements OnInit {
  username: string = '';
  isLoggedIn: boolean = false; // <- Property für Template

  constructor(private keycloakService: KeycloakService) {}

  async ngOnInit() {
    try {
      this.isLoggedIn = await this.keycloakService.isLoggedIn();

      if (this.isLoggedIn) {
        const tokenParsed = this.keycloakService.getKeycloakInstance().tokenParsed;
        this.username = tokenParsed?.['preferred_username'] || tokenParsed?.['name'] || 'User';
        console.log('Loaded username from token:', this.username);
      }
    } catch (error) {
      console.error('Error loading username:', error);
      this.username = 'User';
      this.isLoggedIn = false;
    }
  }

  logout() {
    this.keycloakService.logout(window.location.origin);
  }
}
