import { Routes } from '@angular/router';
import { AnalysisStrategyService } from '@app/analysis/strategy';
import { LocationChangesPageComponent } from './changes/location-changes-page.component';
import { LocationEditPageComponent } from './edit/location-edit-page.component';
import { LocationFactsPageComponent } from './facts/location-facts-page.component';
import { LocationMapPageComponent } from './map/location-map-page.component';
import { LocationMapService } from './map/components/location-map.service';
import { LocationNodesPageComponent } from './nodes/location-nodes-page.component';
import { LocationRoutesPageComponent } from './routes/location-routes-page.component';
import { LocationModeService } from './selection/location-mode.service';
import { LocationSelectionPageComponent } from './selection/location-selection-page.component';
import { LocationSelectionService } from './selection/location-selection.service';

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
        path: ':networkType/:country',
        component: LocationSelectionPageComponent,
      },
      {
        path: ':networkType/:country/:location/nodes',
        component: LocationNodesPageComponent,
      },
      {
        path: ':networkType/:country/:location/routes',
        component: LocationRoutesPageComponent,
      },
      {
        path: ':networkType/:country/:location/facts',
        component: LocationFactsPageComponent,
      },
      {
        path: ':networkType/:country/:location/map',
        component: LocationMapPageComponent,
      },
      {
        path: ':networkType/:country/:location/changes',
        component: LocationChangesPageComponent,
      },
      {
        path: ':networkType/:country/:location/edit',
        component: LocationEditPageComponent,
      },
    ],
  },
];
