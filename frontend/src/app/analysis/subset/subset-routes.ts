import { Routes } from '@angular/router';
import { AnalysisStrategyService } from '@app/analysis/strategy';
import { SubsetChangesPageComponent } from './changes/subset-changes-page.component';
import { SubsetFactDetailsPageComponent } from './fact-details/subset-fact-details-page.component';
import { SubsetFactsPageComponent } from './facts/subset-facts-page.component';
import { SubsetMapPageComponent } from './map/subset-map-page.component';
import { SubsetMapService } from './map/subset-map.service';
import { SubsetNetworksPageComponent } from './networks/subset-networks-page.component';
import { SubsetOrphanNodesPageComponent } from './orphan-nodes/subset-orphan-nodes-page.component';
import { SubsetOrphanRoutesPageComponent } from './orphan-routes/subset-orphan-routes-page.component';

export const subsetRoutes: Routes = [
  {
    path: '',
    providers: [AnalysisStrategyService, SubsetMapService],
    children: [
      {
        path: ':routeType/:country/networks',
        component: SubsetNetworksPageComponent,
      },
      {
        path: ':routeType/:country/facts',
        component: SubsetFactsPageComponent,
      },
      {
        path: ':routeType/:country/orphan-nodes',
        component: SubsetOrphanNodesPageComponent,
      },
      {
        path: ':routeType/:country/orphan-routes',
        component: SubsetOrphanRoutesPageComponent,
      },
      {
        path: ':routeType/:country/map',
        component: SubsetMapPageComponent,
      },
      {
        path: ':routeType/:country/changes',
        component: SubsetChangesPageComponent,
      },
      {
        path: ':routeType/:country/facts/:fact',
        component: SubsetFactDetailsPageComponent,
      },
    ],
  },
];
