/// <reference types="@angular/localize" />

import { enableProdMode } from '@angular/core';
import { bootstrapApplication } from '@angular/platform-browser';
import { Version } from '@app/services';

import { Breadcrumb } from '@sentry/angular';
import { BreadcrumbHint } from '@sentry/angular';
import * as Sentry from '@sentry/angular';
import { AppComponent } from './app/app.component';
import { appConfig } from './app/app.config';
import { environment } from './environments/environment';

if (environment.production) {
  const beforeBreadcrumb = (breadcrumb: Breadcrumb, hint: BreadcrumbHint | undefined) => {
    if (breadcrumb.category === 'ui.click') {
      const { target }: { target: HTMLElement } = hint['event'];
      const id = target.getAttribute('id');
      if (id) {
        breadcrumb.message = '#' + id;
      }
    }
    return breadcrumb;
  };

  // const beforeSend = (event: ErrorEvent, hint: EventHint): ErrorEvent | PromiseLike<ErrorEvent> => {
  //   const headersString = JSON.stringify(event?.request?.headers);
  //   if (headersString.includes('PetalBot')) {
  //     return null;
  //   }
  //   const error = hint.originalException;
  //   if (error && error.toString().includes('ChunkLoadError')) {
  //     window.location.reload();
  //     return null;
  //   }
  //   return event;
  // };

  Sentry.init({
    dsn: 'https://7c2405aac72d47e9b5e5d3fd2ca97a66@o458355.ingest.sentry.io/5455899',
    maxBreadcrumbs: 20,
    maxValueLength: 500,
    release: Version.id,
    beforeBreadcrumb,
    // beforeSend,
  });
  enableProdMode();
}

bootstrapApplication(AppComponent, appConfig).catch((err) => console.log(err));
