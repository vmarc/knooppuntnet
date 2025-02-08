import { Routes } from '@angular/router';
import { AnalysisStrategyService } from '@app/analysis/strategy/analysis-strategy.service';
import { SubsetChangesPageComponent } from './internal/changes/subset-changes-page.component';
import { SubsetFactDetailsPageComponent } from './internal/fact-details/subset-fact-details-page.component';
import { SubsetFactsPageComponent } from './internal/facts/subset-facts-page.component';
import { SubsetMapPageComponent } from './internal/map/subset-map-page.component';
import { SubsetMapService } from './internal/map/subset-map.service';
import { SubsetNetworksPageComponent } from './internal/networks/subset-networks-page.component';
import { SubsetOrphanNodesPageComponent } from './internal/orphan-nodes/subset-orphan-nodes-page.component';
import { SubsetOrphanRoutesPageComponent } from './internal/orphan-routes/subset-orphan-routes-page.component';

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
