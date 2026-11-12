import { KeycloakService } from 'keycloak-angular';
export function initializeKeycloak(keycloak: KeycloakService) {
  return () =>
    keycloak.init({
      config: {
        url: 'https://localhost',
        realm: 'spengermed',
        clientId: 'frontend'
      },
      initOptions: {
        onLoad: 'login-required',
        checkLoginIframe: false,
        enableLogging: true,
        flow: 'standard',
        pkceMethod: 'S256',
        redirectUri: window.location.origin + '/'
      },
      loadUserProfileAtStartUp: false,
      enableBearerInterceptor: true,
      bearerPrefix: 'Bearer',
      bearerExcludedUrls: ['/assets', '/resources']
    });
}
