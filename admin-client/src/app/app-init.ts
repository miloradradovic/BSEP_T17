import {KeycloakEvent, KeycloakEventType, KeycloakService} from 'keycloak-angular';
import {LogInService} from './service/log-in-service/log-in.service';
import {KeycloakLoginOptions} from 'keycloak-js';

export function initializer(keycloak: KeycloakService): any {
  return () =>
    keycloak.init({
      config: {
        url: 'http://localhost:8080/auth',
        realm: 'AdminKeyCloak',
        clientId: 'admin-client',
      },
      initOptions: {
        checkLoginIframe: true,
        checkLoginIframeInterval: 25,
        /*onLoad: 'check-sso',
        checkLoginIframe: true,
        pkceMethod: 'S256'*/
        /*onLoad: 'check-sso',
        silentCheckSsoRedirectUri:
          window.location.origin + '/assets/silent-check-sso.html',*/
      },
      enableBearerInterceptor: true,
      bearerExcludedUrls: ['/']
    });
}


export function initializera(keycloak: KeycloakService, loginService: LogInService): () => Promise<any> {
  return (): Promise<any> => {
    return new Promise<void>(async (resolve, reject) => {
      try {
        await keycloak.init({
          config: {
            url: 'http://localhost:8080/auth',
            realm: 'AdminKeyCloak',
            clientId: 'admin-client',
          },
          initOptions: {
            checkLoginIframe: true,
            checkLoginIframeInterval: 25
            //onLoad: 'check-sso',
            //checkLoginIframe: true,
            //pkceMethod: 'S256'
          },
          enableBearerInterceptor: true,
          bearerExcludedUrls: ['/']
        }).then(async a => {
          console.log('OVOO', a);
          // loginService.logIn(a);  // passing authorization status: true or false to appservice for later use
        });
        resolve();
      } catch (error) {
        console.log('Hell', error);
        reject(error);
      }
    });
  };
}
