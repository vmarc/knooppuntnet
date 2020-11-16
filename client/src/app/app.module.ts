import {LayoutModule} from '@angular/cdk/layout';
import {HTTP_INTERCEPTORS, HttpClientModule} from '@angular/common/http';
import {NgModule} from '@angular/core';
import {ErrorHandler} from '@angular/core';
import {APP_INITIALIZER} from '@angular/core';
import {MatButtonModule} from '@angular/material/button';
import {MatIconModule, MatIconRegistry} from '@angular/material/icon';
import {MatSidenavModule} from '@angular/material/sidenav';
import {BrowserModule} from '@angular/platform-browser';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {ServiceWorkerModule} from '@angular/service-worker';
import {MarkdownModule} from 'ngx-markdown';
import {environment} from '../environments/environment';
import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {AppService} from './app.service';
import {PageWidthService} from './components/shared/page-width.service';
import {PageService} from './components/shared/page.service';
import {SharedModule} from './components/shared/shared.module';
import {I18nService} from './i18n/i18n.service';
import {IconService} from './services/icon.service';
import {LogUpdateService} from './services/log-update.service';
import {PoiService} from './services/poi.service';
import {UserService} from './services/user.service';
import {SpinnerInterceptor} from './spinner/spinner-interceptor';
import {SpinnerModule} from './spinner/spinner.module';
import {SpinnerService} from './spinner/spinner.service';
import * as Sentry from '@sentry/angular';
import {Router} from '@angular/router';
import {VersionService} from './services/version.service';
import {CoreModule} from './core/core.module';

@NgModule({
  declarations: [
    AppComponent
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
    ServiceWorkerModule.register('ngsw-worker.js', {enabled: environment.production})
  ],
  providers: [
    {
      provide: ErrorHandler,
      useValue: Sentry.createErrorHandler({
        showDialog: false,
        dialogOptions: {
          title: 'It looks like we’re having issues in knooppuntnet.'
        }
      }),
    },
    {
      provide: Sentry.TraceService,
      deps: [Router],
    },
    {
      provide: APP_INITIALIZER,
      useFactory: () => () => {
      },
      deps: [Sentry.TraceService],
      multi: true,
    },
    {provide: HTTP_INTERCEPTORS, useClass: SpinnerInterceptor, multi: true},
    UserService,
    VersionService,
    AppService,
    I18nService,
    SpinnerService,
    PageService,
    PageWidthService,
    MatIconRegistry,
    IconService,
    PoiService,
    LogUpdateService,
  ],
  bootstrap: [
    AppComponent
  ]
})
export class AppModule {
}
