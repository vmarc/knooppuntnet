import { Routes } from '@angular/router';
import { provideEffects } from '@ngrx/effects';
import { provideState } from '@ngrx/store';
import { NetworkChangesPageComponent } from './changes/network-changes-page.component';
import { NetworkDetailsPageComponent } from './details/network-details-page.component';
import { NetworkFactsPageComponent } from './facts/network-facts-page.component';
import { NetworkMapPageComponent } from './map/network-map-page.component';
import { NetworkMapService } from './map/components/network-map.service';
import { NetworkNodesPageComponent } from './nodes/network-nodes-page.component';
import { NetworkRoutesPageComponent } from './routes/network-routes-page.component';
import { NetworkEffects } from './store/network.effects';
import { networkReducer } from './store/network.reducer';
import { networkFeatureKey } from './store/network.state';

export const networkRoutes: Routes = [
  {
    path: '',
    providers: [
      provideState({
        name: networkFeatureKey,
        reducer: networkReducer,
      }),
      provideEffects([NetworkEffects]),
      NetworkMapService, // provided here: referenced in effects
    ],
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
