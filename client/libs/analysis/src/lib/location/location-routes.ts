import { Routes } from '@angular/router';
import { AnalysisStrategyService } from '@app/analysis/strategy';
import { Util } from '@app/components/shared';
import { provideEffects } from '@ngrx/effects';
import { provideState } from '@ngrx/store';
import { LocationChangesPageComponent } from './changes/location-changes-page.component';
import { LocationEditPageComponent } from './edit/location-edit-page.component';
import { LocationFactsPageComponent } from './facts/location-facts-page.component';
import { LocationSidebarComponent } from './location-sidebar.component';
import { LocationService } from './location.service';
import { LocationMapPageComponent } from './map/location-map-page.component';
import { LocationMapService } from './map/location-map.service';
import { LocationNodesPageComponent } from './nodes/location-nodes-page.component';
import { LocationNodesSidebarComponent } from './nodes/location-nodes-sidebar.component';
import { LocationRoutesPageComponent } from './routes/location-routes-page.component';
import { LocationRoutesSidebarComponent } from './routes/location-routes-sidebar.component';
import { LocationModeService } from './selection/location-mode.service';
import { LocationSelectionPageComponent } from './selection/location-selection-page.component';
import { LocationSelectionSidebarComponent } from './selection/location-selection-sidebar.component';
import { LocationSelectionService } from './selection/location-selection.service';
import { LocationEffects } from './store/location.effects';
import { locationReducer } from './store/location.reducer';
import { locationFeatureKey } from './store/location.state';

export const locationRoutes: Routes = [
  {
    path: '',
    providers: [
      provideState({
        name: locationFeatureKey,
        reducer: locationReducer,
      }),
      provideEffects([LocationEffects]),
      LocationService,
      LocationModeService,
      LocationSelectionService,
      LocationMapService,
      AnalysisStrategyService,
    ],
    children: [
      Util.routePath(
        ':networkType/:country',
        LocationSelectionPageComponent,
        LocationSelectionSidebarComponent
      ),
      Util.routePath(
        ':networkType/:country/:location/nodes',
        LocationNodesPageComponent,
        LocationNodesSidebarComponent
      ),
      Util.routePath(
        ':networkType/:country/:location/routes',
        LocationRoutesPageComponent,
        LocationRoutesSidebarComponent
      ),
      Util.routePath(
        ':networkType/:country/:location/facts',
        LocationFactsPageComponent,
        LocationSidebarComponent
      ),
      Util.routePath(
        ':networkType/:country/:location/map',
        LocationMapPageComponent,
        LocationSidebarComponent
      ),
      Util.routePath(
        ':networkType/:country/:location/changes',
        LocationChangesPageComponent,
        LocationSidebarComponent
      ),
      Util.routePath(
        ':networkType/:country/:location/edit',
        LocationEditPageComponent,
        LocationSidebarComponent
      ),
    ],
  },
];
