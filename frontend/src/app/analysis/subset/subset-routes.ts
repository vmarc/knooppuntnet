import { Routes } from '@angular/router';
import { AnalysisStrategyService } from '@app/analysis/strategy';
import { SubsetChangesPageComponent } from './changes/subset-changes-page.component';
import { SubsetFactDetailsPageComponent } from './fact-details/subset-fact-details-page.component';
import { SubsetFactsPageComponent } from './facts/subset-facts-page.component';
import { SubsetMapPageComponent } from './map/subset-map-page.component';
import { SubsetMapService } from './map/subset-map.service';
import { SubsetNetworksPageComponent } from './networks/subset-networks-page.component';
import { SubsetOrphanNodesService } from './orphan-nodes/components/subset-orphan-nodes.service';
import { SubsetOrphanNodesPageComponent } from './orphan-nodes/subset-orphan-nodes-page.component';
import { SubsetOrphanRoutesService } from './orphan-routes/components/subset-orphan-routes.service';
import { SubsetOrphanRoutesPageComponent } from './orphan-routes/subset-orphan-routes-page.component';

export const subsetRoutes: Routes = [
  {
    path: '',
    providers: [
      AnalysisStrategyService,
      SubsetMapService,
      SubsetOrphanNodesService,
      SubsetOrphanRoutesService,
    ],
    children: [
      {
        path: ':networkType/:country/networks',
        component: SubsetNetworksPageComponent,
      },
      {
        path: ':networkType/:country/facts',
        component: SubsetFactsPageComponent,
      },
      {
        path: ':networkType/:country/orphan-nodes',
        component: SubsetOrphanNodesPageComponent,
      },
      {
        path: ':networkType/:country/orphan-routes',
        component: SubsetOrphanRoutesPageComponent,
      },
      {
        path: ':networkType/:country/map',
        component: SubsetMapPageComponent,
      },
      {
        path: ':networkType/:country/changes',
        component: SubsetChangesPageComponent,
      },
      {
        path: ':networkType/:country/facts/:fact',
        component: SubsetFactDetailsPageComponent,
      },
    ],
  },
];
