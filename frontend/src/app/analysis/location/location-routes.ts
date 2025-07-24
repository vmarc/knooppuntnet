import { Routes } from '@angular/router';
import { LocationComponent } from '@app/analysis/location/internal/location.component';
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
        path: ':routeType/:country/:location',
        component: LocationComponent,
        children: [
          {
            path: 'details',
            component: LocationDetailsPageComponent,
          },
          {
            path: 'nodes',
            component: LocationNodesPageComponent,
          },
          {
            path: 'routes',
            component: LocationRoutesPageComponent,
          },
          {
            path: 'facts',
            component: LocationFactsPageComponent,
          },
          {
            path: 'map',
            component: LocationMapPageComponent,
          },
          {
            path: 'changes',
            component: LocationChangesPageComponent,
          },
          {
            path: 'edit',
            component: LocationEditPageComponent,
          },
        ],
      },
    ],
  },
];
