import {enableProdMode} from "@angular/core";
import {platformBrowserDynamic} from "@angular/platform-browser-dynamic";
import {AppModule} from "./app/app.module";
import {environment} from "./environments/environment";
import * as Sentry from "@sentry/angular";
import {Integrations} from "@sentry/tracing";

if (environment.production) {
  Sentry.init({
    dsn: "https://7c2405aac72d47e9b5e5d3fd2ca97a66@o458355.ingest.sentry.io/5455899",
    maxBreadcrumbs: 20,
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
