import {
  enableProdMode,
  ErrorHandler,
  APP_INITIALIZER,
  importProvidersFrom,
} from '@angular/core';
import { platformBrowserDynamic } from '@angular/platform-browser-dynamic';

import * as Sentry from '@sentry/angular-ivy';
import { Breadcrumb } from '@sentry/angular-ivy';
import { BreadcrumbHint } from '@sentry/angular-ivy';
import { Event } from '@sentry/angular-ivy';
import { EventHint } from '@sentry/angular-ivy';
import { environment } from './environments/environment';
import { AppComponent } from './app/app.component';
import { ServiceWorkerModule } from '@angular/service-worker';
import { CoreModule } from '@app/core';
import { AppRoutingModule } from './app/app-routing.module';
import { MatButtonModule } from '@angular/material/button';
import { MatSidenavModule } from '@angular/material/sidenav';
import { LayoutModule } from '@angular/cdk/layout';
import { provideAnimations } from '@angular/platform-browser/animations';
import { BrowserModule, bootstrapApplication } from '@angular/platform-browser';
import { MarkdownModule } from 'ngx-markdown';
import { MatIconRegistry, MatIconModule } from '@angular/material/icon';
import {
  PageService,
  PageWidthService,
  SharedModule,
} from '@app/components/shared';
import { I18nService } from '@app/i18n';
import {
  VersionService,
  ApiService,
  IconService,
  PoiService,
  PoiNameService,
  LogUpdateService,
} from '@app/services';
import { SpinnerInterceptor, SpinnerService } from '@app/spinner';
import {
  HTTP_INTERCEPTORS,
  withInterceptorsFromDi,
  provideHttpClient,
} from '@angular/common/http';
import { Router } from '@angular/router';

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
    importProvidersFrom(
      MarkdownModule.forRoot(),
      BrowserModule,
      LayoutModule,
      MatIconModule,
      MatSidenavModule,
      MatButtonModule,
      SharedModule,
      AppRoutingModule,
      CoreModule,
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
    provideAnimations(),
    provideHttpClient(withInterceptorsFromDi()),
  ],
}).catch((err) => console.log(err));
