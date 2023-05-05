import { LayoutModule } from '@angular/cdk/layout';
import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { provideHttpClient } from '@angular/common/http';
import { withInterceptorsFromDi } from '@angular/common/http';
import { isDevMode } from '@angular/core';
import { APP_INITIALIZER } from '@angular/core';
import { enableProdMode } from '@angular/core';
import { ErrorHandler } from '@angular/core';
import { importProvidersFrom } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatDialogModule } from '@angular/material/dialog';
import { MatDialog } from '@angular/material/dialog';
import { MatIconModule, MatIconRegistry } from '@angular/material/icon';
import { MatSidenavModule } from '@angular/material/sidenav';
import { bootstrapApplication, BrowserModule } from '@angular/platform-browser';
import { provideAnimations } from '@angular/platform-browser/animations';
import { withPreloading } from '@angular/router';
import { provideRouter } from '@angular/router';
import { PreloadAllModules } from '@angular/router';
import { Router } from '@angular/router';
import { ServiceWorkerModule } from '@angular/service-worker';
import { AppComponent } from '@app/*';
import { appRoutes } from '@app/*';
import { PageService } from '@app/components/shared';
import { PageWidthService } from '@app/components/shared';
import { metaReducers } from '@app/core';
import { SharedEffects } from '@app/core';
import { UserEffects } from '@app/core';
import { reducers } from '@app/core';
import { I18nService } from '@app/i18n';
import { ApiService } from '@app/services';
import { IconService } from '@app/services';
import { LogUpdateService } from '@app/services';
import { PoiNameService } from '@app/services';
import { PoiService } from '@app/services';
import { VersionService } from '@app/services';
import { SpinnerInterceptor } from '@app/spinner';
import { SpinnerService } from '@app/spinner';
import { provideEffects } from '@ngrx/effects';
import { provideRouterStore } from '@ngrx/router-store';
import { provideStore } from '@ngrx/store';
import { provideStoreDevtools } from '@ngrx/store-devtools';
import * as Sentry from '@sentry/angular-ivy';
import { Breadcrumb } from '@sentry/angular-ivy';
import { BreadcrumbHint } from '@sentry/angular-ivy';
import { Event } from '@sentry/angular-ivy';
import { EventHint } from '@sentry/angular-ivy';
import { MarkdownModule } from 'ngx-markdown';
import { environment } from './environments/environment';

if (environment.production) {
  const beforeBreadcrumb = (
    breadcrumb: Breadcrumb,
    hint: BreadcrumbHint | undefined
  ) => {
    if (breadcrumb.category === 'ui.click') {
      const { target }: { target: HTMLElement } = hint.event;
      const id = target.getAttribute('id');
      if (id) {
        breadcrumb.message = '#' + id;
      }
    }
    return breadcrumb;
  };

  const beforeSend = (event: Event, hint: EventHint | undefined) => {
    const headersString = JSON.stringify(event?.request?.headers);
    if (headersString.includes('PetalBot')) {
      return null;
    }
    const error = hint.originalException;
    if (error && error.toString().includes('ChunkLoadError')) {
      window.location.reload();
      return null;
    }
    return event;
  };

  Sentry.init({
    dsn: 'https://7c2405aac72d47e9b5e5d3fd2ca97a66@o458355.ingest.sentry.io/5455899',
    maxBreadcrumbs: 12,
    maxValueLength: 500,
    beforeBreadcrumb,
    beforeSend,
  });
  enableProdMode();
}

bootstrapApplication(AppComponent, {
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
}).catch((err) => console.log(err));
