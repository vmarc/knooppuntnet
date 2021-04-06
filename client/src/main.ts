import {enableProdMode} from '@angular/core';
import {platformBrowserDynamic} from '@angular/platform-browser-dynamic';
import {AppModule} from '@app/app.module';
import * as Sentry from '@sentry/angular';
import {Breadcrumb, BreadcrumbHint, Event, EventHint} from '@sentry/angular';
import {environment} from './environments/environment';

if (environment.production) {

  const beforeBreadcrumb = (breadcrumb: Breadcrumb, hint: BreadcrumbHint | undefined) => {
    if (breadcrumb.category === 'ui.click') {
      const {target}: { target: HTMLElement } = hint.event;
      const id = target.getAttribute('id');
      if (id) {
        breadcrumb.message = '#' + id;
      }
    }
    return breadcrumb;
  };

  const beforeSend = (event: Event, hint: EventHint | undefined) => {
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
    beforeBreadcrumb,
    beforeSend
  });
  enableProdMode();
}

platformBrowserDynamic().bootstrapModule(AppModule)
  .catch(err => console.log(err));
