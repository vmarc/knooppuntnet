import { LayoutModule } from '@angular/cdk/layout';
import { OverlayContainer } from '@angular/cdk/overlay';
import { FullscreenOverlayContainer } from '@angular/cdk/overlay';
import { provideHttpClient } from '@angular/common/http';
import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { withInterceptorsFromDi } from '@angular/common/http';
import { ApplicationConfig } from '@angular/core';
import { importProvidersFrom } from '@angular/core';
import { ErrorHandler } from '@angular/core';
import { APP_INITIALIZER } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatDialogModule } from '@angular/material/dialog';
import { MatDialog } from '@angular/material/dialog';
import { MatIconModule, MatIconRegistry } from '@angular/material/icon';
import { MatSidenavModule } from '@angular/material/sidenav';
import { BrowserModule } from '@angular/platform-browser';
import { provideAnimations } from '@angular/platform-browser/animations';
import { provideRouter } from '@angular/router';
import { Router } from '@angular/router';
import { EditService } from '@app/components/shared';
import { PageService } from '@app/components/shared';
import { PageWidthService } from '@app/components/shared';
import { PreferencesService } from '@app/core';
import { ApiService } from '@app/services';
import { IconService } from '@app/services';
import { PoiService } from '@app/services';
import { PoiNameService } from '@app/services';
import { SpinnerInterceptor } from '@app/spinner';
import { SpinnerService } from '@app/spinner';
import * as Sentry from '@sentry/angular';
import { AngularSplitModule } from 'angular-split';
import { MarkdownModule } from 'ngx-markdown';
import { appRoutes } from './app-routes';
import { MapService } from './map/map.service';
import { RootService } from './root/root.service';
import { UserService } from './shared/user';
import { StateService } from '@app/state';

export const appConfig: ApplicationConfig = {
  providers: [
    provideRouter(
      appRoutes
      // withRouterConfig({
      //   onSameUrlNavigation: 'reload',
      // })
      // withPreloading(PreloadAllModules)
      // withDebugTracing()
    ),
    // { provide: RouteReuseStrategy, useClass: KpnRouteReuseStrategy },
    importProvidersFrom(
      MarkdownModule.forRoot(),
      BrowserModule,
      LayoutModule,
      MatIconModule,
      MatSidenavModule,
      MatButtonModule,
      MatDialogModule,
      AngularSplitModule
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
    { provide: OverlayContainer, useClass: FullscreenOverlayContainer },
    PreferencesService,
    ApiService,
    SpinnerService,
    RootService,
    StateService,
    PageService,
    PageWidthService,
    MapService,
    MatIconRegistry,
    IconService,
    PoiService,
    PoiNameService,
    EditService,
    MatDialog,
    provideAnimations(),
    provideHttpClient(withInterceptorsFromDi()),
    { provide: UserService },
  ],
};
