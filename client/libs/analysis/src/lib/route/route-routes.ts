import { Routes } from '@angular/router';
import { Util } from '@app/components/shared';
import { AnalysisSidebarComponent } from '@app/components/shared/sidebar';
import { provideEffects } from '@ngrx/effects';
import { provideState } from '@ngrx/store';
import { RouteChangesPageComponent } from './changes/_route-changes-page.component';
import { RouteChangesSidebarComponent } from './changes/route-changes-sidebar.component';
import { RoutePageComponent } from './details/_route-page.component';
import { RouteMapPageComponent } from './map/_route-map-page.component';
import { RouteMapService } from './map/route-map.service';
import { RouteEffects } from './store/route.effects';
import { routeReducer } from './store/route.reducer';
import { routeFeatureKey } from './store/route.state';

export const routeRoutes: Routes = [
  {
    path: '',
    providers: [
      provideState({
        name: routeFeatureKey,
        reducer: routeReducer,
      }),
      provideEffects([RouteEffects]),
      RouteMapService, // provided here: referenced in effects
    ],
    children: [
      Util.routePath(':routeId', RoutePageComponent, AnalysisSidebarComponent),
      Util.routePath(
        ':routeId/map',
        RouteMapPageComponent,
        AnalysisSidebarComponent
      ),
      Util.routePath(
        ':routeId/changes',
        RouteChangesPageComponent,
        RouteChangesSidebarComponent
      ),
    ],
  },
];
