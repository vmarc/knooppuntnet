import { Routes } from '@angular/router';
import { AnalysisStrategyService } from '@app/analysis/strategy/analysis-strategy.service';
import { LocationChangesPageComponent } from './internal/changes/location-changes-page.component';
import { LocationDetailsPageComponent } from './internal/details/location-details-page.component';
import { LocationEditPageComponent } from './internal/edit/location-edit-page.component';
import { LocationFactsPageComponent } from './internal/facts/location-facts-page.component';
import { LocationMapService } from './internal/map/components/location-map.service';
import { LocationMapPageComponent } from './internal/map/location-map-page.component';
import { LocationNodesPageComponent } from './internal/nodes/location-nodes-page.component';
import { LocationRoutesPageComponent } from './internal/routes/location-routes-page.component';
import { LocationModeService } from './internal/selection/components/location-mode.service';
import { LocationSelectionPageComponent } from './internal/selection/location-selection-page.component';
import { LocationSelectionService } from './internal/selection/location-selection.service';

export const locationRoutes: Routes = [
  {
    path: '',
    providers: [
      AnalysisStrategyService,
      LocationMapService,
      LocationModeService,
      LocationSelectionService,
    ],
    children: [
      {
        path: ':routeType/:country',
        component: LocationSelectionPageComponent,
      },
      {
        path: ':routeType/:country/:location/details',
        component: LocationDetailsPageComponent,
      },
      {
        path: ':routeType/:country/:location/nodes',
        component: LocationNodesPageComponent,
      },
      {
        path: ':routeType/:country/:location/routes',
        component: LocationRoutesPageComponent,
      },
      {
        path: ':routeType/:country/:location/facts',
        component: LocationFactsPageComponent,
      },
      {
        path: ':routeType/:country/:location/map',
        component: LocationMapPageComponent,
      },
      {
        path: ':routeType/:country/:location/changes',
        component: LocationChangesPageComponent,
      },
      {
        path: ':routeType/:country/:location/edit',
        component: LocationEditPageComponent,
      },
    ],
  },
];
