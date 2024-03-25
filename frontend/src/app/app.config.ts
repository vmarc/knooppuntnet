import { LayoutModule } from '@angular/cdk/layout';
import { provideHttpClient } from '@angular/common/http';
import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { withInterceptorsFromDi } from '@angular/common/http';
import { ApplicationConfig } from '@angular/core';
import { importProvidersFrom } from '@angular/core';
import { ErrorHandler } from '@angular/core';
import { APP_INITIALIZER } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatDialogModule } from '@angular/material/dialog';
import { MatDialog } from '@angular/material/dialog';
import { MatIconModule, MatIconRegistry } from '@angular/material/icon';
import { MatSidenavModule } from '@angular/material/sidenav';
import { BrowserModule } from '@angular/platform-browser';
import { provideAnimations } from '@angular/platform-browser/animations';
import { provideRouter } from '@angular/router';
import { withPreloading } from '@angular/router';
import { PreloadAllModules } from '@angular/router';
import { Router } from '@angular/router';
import { appRoutes } from '@app/*';
import { EditService } from '@app/components/shared';
import { PageService } from '@app/components/shared';
import { PageWidthService } from '@app/components/shared';
import { PreferencesService } from '@app/core';
import { VersionService } from '@app/services';
import { ApiService } from '@app/services';
import { IconService } from '@app/services';
import { PoiService } from '@app/services';
import { PoiNameService } from '@app/services';
import { SpinnerInterceptor } from '@app/spinner';
import { SpinnerService } from '@app/spinner';
import * as Sentry from '@sentry/angular-ivy';
import { provideOAuthClient } from 'angular-oauth2-oidc';
import { MarkdownModule } from 'ngx-markdown';
import { UserService } from './shared/user';

export const appConfig: ApplicationConfig = {
  providers: [
    provideRouter(
      appRoutes,
      withPreloading(PreloadAllModules)
      // withDebugTracing()
    ),
    importProvidersFrom(
      MarkdownModule.forRoot(),
      BrowserModule,
      LayoutModule,
      MatIconModule,
      MatSidenavModule,
      MatButtonModule,
      MatDialogModule
    ),
    {
      provide: ErrorHandler,
      useValue: Sentry.createErrorHandler({
        showDialog: false,
      }),
    },
    {
      provide: Sentry.TraceService,
      deps: [Router],
    },
    {
      provide: APP_INITIALIZER,
      useFactory: () => () => {},
      deps: [Sentry.TraceService],
      multi: true,
    },
    { provide: HTTP_INTERCEPTORS, useClass: SpinnerInterceptor, multi: true },
    PreferencesService,
    VersionService,
    ApiService,
    SpinnerService,
    PageService,
    PageWidthService,
    MatIconRegistry,
    IconService,
    PoiService,
    PoiNameService,
    EditService,
    MatDialog,
    provideAnimations(),
    provideHttpClient(withInterceptorsFromDi()),
    provideOAuthClient(),
    { provide: UserService },
  ],
};
