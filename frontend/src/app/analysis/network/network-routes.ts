import { Routes } from '@angular/router';
import { NetworkChangesPageComponent } from './changes/network-changes-page.component';
import { NetworkDetailsPageComponent } from './details/network-details-page.component';
import { NetworkFactsPageComponent } from './facts/network-facts-page.component';
import { NetworkMapPageComponent } from './map/network-map-page.component';
import { NetworkNodesPageComponent } from './nodes/network-nodes-page.component';
import { NetworkRoutesPageComponent } from './routes/network-routes-page.component';

export const networkRoutes: Routes = [
  {
    path: '',
    children: [
      {
        path: ':networkId',
        component: NetworkDetailsPageComponent,
      },
      {
        path: ':networkId/facts',
        component: NetworkFactsPageComponent,
      },
      {
        path: ':networkId/nodes',
        component: NetworkNodesPageComponent,
      },
      {
        path: ':networkId/routes',
        component: NetworkRoutesPageComponent,
      },
      {
        path: ':networkId/map',
        component: NetworkMapPageComponent,
      },
      {
        path: ':networkId/changes',
        component: NetworkChangesPageComponent,
      },
    ],
  },
];
