import { LayoutModule } from '@angular/cdk/layout';
import { provideHttpClient } from '@angular/common/http';
import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { withInterceptorsFromDi } from '@angular/common/http';
import { ApplicationConfig } from '@angular/core';
import { importProvidersFrom } from '@angular/core';
import { ErrorHandler } from '@angular/core';
import { APP_INITIALIZER } from '@angular/core';
import { isDevMode } from '@angular/core';
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
import { ServiceWorkerModule } from '@angular/service-worker';
import { appRoutes } from '@app/*';
import { EditService } from '@app/components/shared';
import { PageService } from '@app/components/shared';
import { PageWidthService } from '@app/components/shared';
import { reducers } from '@app/core';
import { metaReducers } from '@app/core';
import { SharedEffects } from '@app/core';
import { UserEffects } from '@app/core';
import { I18nService } from '@app/i18n';
import { VersionService } from '@app/services';
import { ApiService } from '@app/services';
import { IconService } from '@app/services';
import { PoiService } from '@app/services';
import { PoiNameService } from '@app/services';
import { LogUpdateService } from '@app/services';
import { SpinnerInterceptor } from '@app/spinner';
import { SpinnerService } from '@app/spinner';
import { provideEffects } from '@ngrx/effects';
import { provideRouterStore } from '@ngrx/router-store';
import { provideStore } from '@ngrx/store';
import { provideStoreDevtools } from '@ngrx/store-devtools';
import * as Sentry from '@sentry/angular-ivy';
import { MarkdownModule } from 'ngx-markdown';
import { environment } from '../environments/environment';

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
      MatDialogModule,
      ServiceWorkerModule.register('ngsw-worker.js', {
        enabled: environment.production,
      })
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
    VersionService,
    ApiService,
    I18nService,
    SpinnerService,
    PageService,
    PageWidthService,
    MatIconRegistry,
    IconService,
    PoiService,
    PoiNameService,
    EditService,
    LogUpdateService,
    MatDialog,
    provideAnimations(),
    provideHttpClient(withInterceptorsFromDi()),
    provideStore(reducers, {
      metaReducers,
      runtimeChecks: {
        strictStateImmutability: true,
        strictActionImmutability: true,
        strictStateSerializability: false,
        strictActionSerializability: false,
        strictActionWithinNgZone: true,
        strictActionTypeUniqueness: true,
      },
    }),
    provideEffects([SharedEffects, UserEffects]),
    provideRouterStore(),
    provideStoreDevtools({
      name: 'Knooppuntnet',
      maxAge: 25,
      logOnly: !isDevMode(),
      autoPause: true,
      trace: false,
      traceLimit: 75,
      serialize: {
        options: {
          map: true,
        },
      },
    }),
  ],
};
