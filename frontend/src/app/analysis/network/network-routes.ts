import { Routes } from '@angular/router';
import { NetworkComponent } from './internal/network.component';
import { NetworkChangesPageComponent } from './internal/changes/network-changes-page.component';
import { NetworkDetailsPageComponent } from './internal/details/network-details-page.component';
import { NetworkFactsPageComponent } from './internal/facts/network-facts-page.component';
import { NetworkMapPageComponent } from './internal/map/network-map-page.component';
import { NetworkNodesPageComponent } from './internal/nodes/network-nodes-page.component';
import { NetworkRoutesPageComponent } from './internal/routes/network-routes-page.component';

export const networkRoutes: Routes = [
  {
    path: ':networkId',
    component: NetworkComponent,
    children: [
      {
        path: '',
        component: NetworkDetailsPageComponent,
      },
      {
        path: 'facts',
        component: NetworkFactsPageComponent,
      },
      {
        path: 'nodes',
        component: NetworkNodesPageComponent,
      },
      {
        path: 'routes',
        component: NetworkRoutesPageComponent,
      },
      {
        path: 'map',
        component: NetworkMapPageComponent,
      },
      {
        path: 'changes',
        component: NetworkChangesPageComponent,
      },
    ],
  },
];
