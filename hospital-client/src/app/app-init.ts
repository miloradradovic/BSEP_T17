import {KeycloakService} from 'keycloak-angular';
import jwtDecode from 'jwt-decode';
import {StorageService} from './services/storage/storage.service';

export function initializer(keycloak: KeycloakService, storageService: StorageService): any {
  return () => {
    keycloak.init({
      config: {
        url: 'http://localhost:8080/auth',
        realm: 'hospital-portal',
        clientId: 'hospital-client',
      },
      initOptions: {
        onLoad: 'login-required',
        checkLoginIframe: false,
        /*onLoad: 'check-sso',
        checkLoginIframe: true,
        pkceMethod: 'S256'*/
        /*onLoad: 'check-sso',
        silentCheckSsoRedirectUri:
          window.location.origin + '/assets/silent-check-sso.html',*/
      },
      enableBearerInterceptor: true,
      bearerPrefix: 'Bearer',
      bearerExcludedUrls: [
        '/assets']
    }).then(() => {
      const decoded = jwtDecode(keycloak.getKeycloakInstance().token);

      const user = {
        email: decoded['email'],
        token: keycloak.getKeycloakInstance().token,
        role: decoded['realm_access']['roles'].includes('ADMIN') ? 'ADMIN' : 'DOCTOR'
      };
      storageService.setStorageItem('user', JSON.stringify(user));
    });
  };
}


export function initializer_try(keycloak: KeycloakService, storageService: StorageService): () => Promise<any> {
  return (): Promise<any> => {
    return new Promise<void>(async (resolve, reject) => {
      try {
        await
          keycloak.init({
            config: {
              url: 'http://localhost:8080/auth',
              realm: 'admin-portal',
              clientId: 'admin-client',
            },
            initOptions: {
              onLoad: 'login-required',
              checkLoginIframe: false,
              /*onLoad: 'check-sso',
              checkLoginIframe: true,
              pkceMethod: 'S256'*/
              /*onLoad: 'check-sso',
              silentCheckSsoRedirectUri:
                window.location.origin + '/assets/silent-check-sso.html',*/
            },
            enableBearerInterceptor: true,
            bearerPrefix: 'Bearer',
            bearerExcludedUrls: [
              '/assets',
              '/clients/public']
          }).then(() => {
            if (keycloak.getKeycloakInstance().refreshToken) {
              console.log('hi');
            }
            const decoded = jwtDecode(keycloak.getKeycloakInstance().token);

            const user = {
              email: decoded['email'],
              token: keycloak.getKeycloakInstance().token,
              role: decoded['realm_access']['roles'][0]
            };
            storageService.setStorageItem('user', JSON.stringify(user));
          });
        debugger
        resolve();
      } catch (error) {
        reject(error);
      }
    }).catch((e) => {
      console.log('Error thrown in init ' + e);
    });
  };
}
