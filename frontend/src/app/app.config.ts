import { LayoutModule } from '@angular/cdk/layout';
import { OverlayContainer } from '@angular/cdk/overlay';
import { FullscreenOverlayContainer } from '@angular/cdk/overlay';
import { provideHttpClient } from '@angular/common/http';
import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { withInterceptorsFromDi } from '@angular/common/http';
import { ApplicationConfig } from '@angular/core';
import { inject } from '@angular/core';
import { provideAppInitializer } from '@angular/core';
import { importProvidersFrom } from '@angular/core';
import { ErrorHandler } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { BrowserModule } from '@angular/platform-browser';
import { withComponentInputBinding } from '@angular/router';
import { provideRouter } from '@angular/router';
import { Router } from '@angular/router';
import { PreferencesService } from '@app/shared/core/preferences/preferences.service';
import { ApiService } from '@app/shared/services/api.service';
import { OldPoiService } from '@app/shared/services/old-poi.service';
import { OldPoiNameService } from '@app/shared/services/old-poi-name.service';
import { EditService } from '@app/shared/components/edit.service';
import { PageWidthService } from '@app/shared/components/page-width.service';
import { PageService } from '@app/shared/components/page.service';
import { SpinnerInterceptor } from '@app/shared/spinner/spinner-interceptor';
import { SpinnerService } from '@app/shared/spinner/spinner.service';
import * as Sentry from '@sentry/angular';
import { NzModalService } from 'ng-zorro-antd/modal';
import { provideMarkdown } from 'ngx-markdown';
import { appRoutes } from './app-routes';
import { MapInteractionsService } from './map/map-interactions.service';
import { MapService } from './map/map.service';
import { PoiService } from './map/poi/poi.service';
import { MapRoutePopupInteractionService } from './map/popup/map-route-popup-interaction.service';
import { PlannerPopupService } from './planner/domain/context/planner-popup-service';
import { PlannerMapLayerService } from './planner/pages/planner/planner-map-layer.service';
import { PlannerStateService } from './planner/pages/planner/planner-state.service';
import { RootService } from './root/internal/root.service';
import { RouterService } from './shared/services/router.service';
import { UserService } from '@app/shared/user/user.service';
import { State } from '@app/state/state';
import { en_US, provideNzI18n } from 'ng-zorro-antd/i18n';
import { registerLocaleData } from '@angular/common';
import en from '@angular/common/locales/en';
import { FormsModule } from '@angular/forms';

registerLocaleData(en);

export const appConfig: ApplicationConfig = {
  providers: [
    provideRouter(
      appRoutes,
      withComponentInputBinding()
      // withRouterConfig({
      //   onSameUrlNavigation: 'reload',
      // })
      // withPreloading(PreloadAllModules)
      // withDebugTracing()
    ),
    // { provide: RouteReuseStrategy, useClass: KpnRouteReuseStrategy },
    importProvidersFrom(BrowserModule, LayoutModule, MatButtonModule),
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
    provideAppInitializer(() => {
      inject(Sentry.TraceService);
    }),
    { provide: HTTP_INTERCEPTORS, useClass: SpinnerInterceptor, multi: true },
    { provide: OverlayContainer, useClass: FullscreenOverlayContainer },
    ApiService,
    EditService,
    MapInteractionsService,
    MapRoutePopupInteractionService,
    MapService,
    NzModalService,
    OldPoiNameService,
    OldPoiService,
    PageService,
    PageWidthService,
    PlannerMapLayerService,
    PlannerPopupService,
    PlannerStateService,
    PoiService,
    PreferencesService,
    RootService,
    RouterService,
    SpinnerService,
    State,
    provideHttpClient(withInterceptorsFromDi()),
    { provide: UserService },
    provideNzI18n(en_US),
    importProvidersFrom(FormsModule),
    provideHttpClient(),
    provideMarkdown(),
  ],
};
