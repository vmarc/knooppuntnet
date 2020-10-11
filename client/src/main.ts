import {platformBrowserDynamic} from "@angular/platform-browser-dynamic";
import {AppModule} from "./app/app.module";
import {environment} from "./environments/environment";
import * as Sentry from "@sentry/angular";
import {Integrations} from "@sentry/tracing";
import {enableProdMode} from "@angular/core";

if (environment.production) {
  Sentry.init({
    dsn: "https://7c2405aac72d47e9b5e5d3fd2ca97a66@o458355.ingest.sentry.io/5455899",
    maxBreadcrumbs: 12,
    beforeBreadcrumb(breadcrumb, hint) {
      if (breadcrumb.category === "ui.click") {
        const {target}: { target: HTMLElement } = hint.event;
        const id = target.getAttribute("id");
        if (id) {
          breadcrumb.message = "#" + id;
        }
      }
      return breadcrumb;
    },
    integrations: [
      new Integrations.BrowserTracing({
        tracingOrigins: [
          "localhost",
          "https://experimental.knooppuntnet.nl/json-api"
        ],
        routingInstrumentation: Sentry.routingInstrumentation,
      }),
    ],
    tracesSampleRate: 1.0,
  });
  enableProdMode();
}

platformBrowserDynamic().bootstrapModule(AppModule)
  .catch(err => console.log(err));
