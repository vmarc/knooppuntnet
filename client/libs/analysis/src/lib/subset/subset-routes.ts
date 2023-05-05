import { Routes } from '@angular/router';
import { AnalysisStrategyService } from '@app/analysis/strategy';
import { Util } from '@app/components/shared';
import { provideEffects } from '@ngrx/effects';
import { provideState } from '@ngrx/store';
import { SubsetChangesPageComponent } from './changes/_subset-changes-page.component';
import { SubsetChangesSidebarComponent } from './changes/subset-changes-sidebar.component';
import { SubsetFactDetailsPageComponent } from './fact-details/_subset-fact-details-page.component';
import { SubsetFactsPageComponent } from './facts/_subset-facts-page.component';
import { SubsetMapPageComponent } from './map/_subset-map-page.component';
import { SubsetMapService } from './map/subset-map.service';
import { SubsetNetworksPageComponent } from './networks/_subset-networks-page.component';
import { SubsetOrphanNodesPageComponent } from './orphan-nodes/_subset-orphan-nodes-page.component';
import { SubsetOrphanNodesSidebarComponent } from './orphan-nodes/subset-orphan-nodes-sidebar.component';
import { SubsetOrphanNodesService } from './orphan-nodes/subset-orphan-nodes.service';
import { SubsetOrphanRoutesPageComponent } from './orphan-routes/_subset-orphan-routes-page.component';
import { SubsetOrphanRoutesSidebarComponent } from './orphan-routes/subset-orphan-routes-sidebar.component';
import { SubsetOrphanRoutesService } from './orphan-routes/subset-orphan-routes.service';
import { SubsetEffects } from './store/subset.effects';
import { subsetReducer } from './store/subset.reducer';
import { subsetFeatureKey } from './store/subset.state';
import { SubsetSidebarComponent } from './subset-sidebar.component';

export const subsetRoutes: Routes = [
  {
    path: '',
    providers: [
      provideState({
        name: subsetFeatureKey,
        reducer: subsetReducer,
      }),
      provideEffects([SubsetEffects]),
      SubsetOrphanNodesService,
      SubsetOrphanRoutesService,
      SubsetMapService,
      AnalysisStrategyService,
    ],
    children: [
      Util.routePath(
        ':networkType/:country/networks',
        SubsetNetworksPageComponent,
        SubsetSidebarComponent
      ),
      Util.routePath(
        ':networkType/:country/facts',
        SubsetFactsPageComponent,
        SubsetSidebarComponent
      ),
      Util.routePath(
        ':networkType/:country/orphan-nodes',
        SubsetOrphanNodesPageComponent,
        SubsetOrphanNodesSidebarComponent
      ),
      Util.routePath(
        ':networkType/:country/orphan-routes',
        SubsetOrphanRoutesPageComponent,
        SubsetOrphanRoutesSidebarComponent
      ),
      Util.routePath(
        ':networkType/:country/map',
        SubsetMapPageComponent,
        SubsetSidebarComponent
      ),
      Util.routePath(
        ':networkType/:country/changes',
        SubsetChangesPageComponent,
        SubsetChangesSidebarComponent
      ),
      Util.routePath(
        ':networkType/:country/facts/:fact',
        SubsetFactDetailsPageComponent,
        SubsetSidebarComponent
      ),
    ],
  },
];
