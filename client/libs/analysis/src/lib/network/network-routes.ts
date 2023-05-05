import { Routes } from '@angular/router';
import { Util } from '@app/components/shared';
import { AnalysisSidebarComponent } from '@app/components/shared/sidebar';
import { provideEffects } from '@ngrx/effects';
import { provideState } from '@ngrx/store';
import { NetworkChangesPageComponent } from './changes/_network-changes-page.component';
import { NetworkChangesSidebarComponent } from './changes/network-changes-sidebar.component';
import { NetworkDetailsPageComponent } from './details/_network-details-page.component';
import { NetworkFactsPageComponent } from './facts/_network-facts-page.component';
import { NetworkMapPageComponent } from './map/_network-map-page.component';
import { NetworkMapSidebarComponent } from './map/network-map-sidebar.component';
import { NetworkMapService } from './map/network-map.service';
import { NetworkNodesPageComponent } from './nodes/_network-nodes-page.component';
import { NetworkNodesSidebarComponent } from './nodes/network-nodes-sidebar.component';
import { NetworkRoutesPageComponent } from './routes/_network-routes-page.component';
import { NetworkRoutesSidebarComponent } from './routes/network-routes-sidebar.component';
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
      Util.routePath(
        ':networkId',
        NetworkDetailsPageComponent,
        AnalysisSidebarComponent
      ),
      Util.routePath(
        ':networkId/facts',
        NetworkFactsPageComponent,
        AnalysisSidebarComponent
      ),
      Util.routePath(
        ':networkId/nodes',
        NetworkNodesPageComponent,
        NetworkNodesSidebarComponent
      ),
      Util.routePath(
        ':networkId/routes',
        NetworkRoutesPageComponent,
        NetworkRoutesSidebarComponent
      ),
      Util.routePath(
        ':networkId/map',
        NetworkMapPageComponent,
        NetworkMapSidebarComponent
      ),
      Util.routePath(
        ':networkId/changes',
        NetworkChangesPageComponent,
        NetworkChangesSidebarComponent
      ),
    ],
  },
];
