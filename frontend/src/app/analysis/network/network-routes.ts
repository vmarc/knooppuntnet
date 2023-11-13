import { Routes } from '@angular/router';
import { provideEffects } from '@ngrx/effects';
import { provideState } from '@ngrx/store';
import { NetworkChangesPageComponent } from './changes/_network-changes-page.component';
import { NetworkDetailsPageComponent } from './details/_network-details-page.component';
import { NetworkFactsPageComponent } from './facts/_network-facts-page.component';
import { NetworkMapPageComponent } from './map/_network-map-page.component';
import { NetworkMapService } from './map/network-map.service';
import { NetworkNodesPageComponent } from './nodes/_network-nodes-page.component';
import { NetworkRoutesPageComponent } from './routes/_network-routes-page.component';
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
