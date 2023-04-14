import { LayoutModule } from '@angular/cdk/layout';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { NgModule } from '@angular/core';
import { ErrorHandler } from '@angular/core';
import { APP_INITIALIZER } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule, MatIconRegistry } from '@angular/material/icon';
import { MatSidenavModule } from '@angular/material/sidenav';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { Router } from '@angular/router';
import { ServiceWorkerModule } from '@angular/service-worker';
import { PageWidthService } from '@app/components/shared';
import { PageService } from '@app/components/shared';
import { SharedModule } from '@app/components/shared';
import { CoreModule } from '@app/core';
import { I18nService } from '@app/i18n';
import { IconService } from '@app/services';
import { LogUpdateService } from '@app/services';
import { PoiNameService } from '@app/services';
import { PoiService } from '@app/services';
import { VersionService } from '@app/services';
import { SpinnerInterceptor } from '@app/spinner';
import { SpinnerModule } from '@app/spinner';
import { SpinnerService } from '@app/spinner';
import * as Sentry from '@sentry/angular-ivy';
import { MarkdownModule } from 'ngx-markdown';
// eslint-disable-next-line @softarc/sheriff/deep-import
import { environment } from '../environments/environment';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { AppService } from './app.service';
import { JosmComponent } from './josm.component';

@NgModule({
  declarations: [
    AppComponent,
    JosmComponent, // temporary
  ],
  imports: [
    MarkdownModule.forRoot(),
    BrowserModule,
    BrowserAnimationsModule,
    LayoutModule,
    MatIconModule,
    MatSidenavModule,
    MatButtonModule,
    HttpClientModule,
    SharedModule,
    AppRoutingModule,
    CoreModule,
    SpinnerModule,
    ServiceWorkerModule.register('ngsw-worker.js', {
      enabled: environment.production,
    }),
  ],
  providers: [
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
    AppService,
    I18nService,
    SpinnerService,
    PageService,
    PageWidthService,
    MatIconRegistry,
    IconService,
    PoiService,
    PoiNameService,
    LogUpdateService,
  ],
  bootstrap: [AppComponent],
})
export class AppModule {}
