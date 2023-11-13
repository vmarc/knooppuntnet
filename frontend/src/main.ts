/// <reference types="@angular/localize" />

import { enableProdMode } from '@angular/core';
import { bootstrapApplication } from '@angular/platform-browser';

import { AppComponent } from '@app/*';

import { Breadcrumb } from '@sentry/angular-ivy';
import { BreadcrumbHint } from '@sentry/angular-ivy';
import { Event } from '@sentry/angular-ivy';
import { EventHint } from '@sentry/angular-ivy';
import * as Sentry from '@sentry/angular-ivy';
import { appConfig } from './app/app.config';
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

bootstrapApplication(AppComponent, appConfig).catch((err) => console.log(err));
