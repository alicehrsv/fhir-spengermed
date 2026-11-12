import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { KeycloakService } from 'keycloak-angular';
export const authGuard: CanActivateFn = async (route, state) => {
  const keycloakService = inject(KeycloakService);
  const router = inject(Router);
  const authenticated = await keycloakService.isLoggedIn();
  if (!authenticated) {
    await keycloakService.login({
      redirectUri: window.location.origin + state.url
    });
    return false;
  }
  const requiredRoles = route.data['roles'] as string[];
  if (requiredRoles && requiredRoles.length > 0) {
    const hasRole = requiredRoles.some(role => keycloakService.isUserInRole(role));
    if (!hasRole) {
      router.navigate(['/unauthorized']);
      return false;
    }
  }
  return true;
};
