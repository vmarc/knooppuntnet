import { Routes } from '@angular/router';
import { AnalysisStrategyService } from '@app/analysis/strategy/analysis-strategy.service';
import { SubsetComponent } from '@app/analysis/subset/internal/subset.component';
import { SubsetChangesPageComponent } from './internal/changes/subset-changes-page.component';
import { SubsetFactDetailsPageComponent } from './internal/fact-details/subset-fact-details-page.component';
import { SubsetFactsPageComponent } from './internal/facts/subset-facts-page.component';
import { SubsetNetworksPageComponent } from './internal/networks/subset-networks-page.component';
import { SubsetOrphanNodesPageComponent } from './internal/orphan-nodes/subset-orphan-nodes-page.component';
import { SubsetOrphanRoutesPageComponent } from './internal/orphan-routes/subset-orphan-routes-page.component';

export const subsetRoutes: Routes = [
  {
    path: ':routeType/:country',
    component: SubsetComponent,
    providers: [AnalysisStrategyService],
    children: [
      {
        path: 'networks',
        component: SubsetNetworksPageComponent,
      },
      {
        path: 'facts',
        component: SubsetFactsPageComponent,
      },
      {
        path: 'orphan-nodes',
        component: SubsetOrphanNodesPageComponent,
      },
      {
        path: 'orphan-routes',
        component: SubsetOrphanRoutesPageComponent,
      },
      {
        path: 'changes',
        component: SubsetChangesPageComponent,
      },
      {
        path: 'facts/:fact',
        component: SubsetFactDetailsPageComponent,
      },
    ],
  },
];
