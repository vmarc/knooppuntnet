import { provideHttpClient } from "@angular/common/http";
import { ApplicationConfig } from '@angular/core';
import { provideRouter } from '@angular/router';
import { provideOAuthClient } from "angular-oauth2-oidc";

import { routes } from './app.routes';
import { authModuleConfig } from "./auth-module-config";
import { UserService } from "./service/user.service";

export const appConfig: ApplicationConfig = {
  providers: [
    provideRouter(routes),
    provideHttpClient(),
    provideOAuthClient(authModuleConfig),
    {provide: UserService},
  ]
};
