import { Routes } from '@angular/router';
import { AnalysisStrategyService } from '@app/analysis/strategy';
import { provideEffects } from '@ngrx/effects';
import { provideState } from '@ngrx/store';
import { SubsetChangesPageComponent } from './changes/subset-changes-page.component';
import { SubsetFactDetailsPageComponent } from './fact-details/subset-fact-details-page.component';
import { SubsetFactsPageComponent } from './facts/subset-facts-page.component';
import { SubsetMapPageComponent } from './map/subset-map-page.component';
import { SubsetMapService } from './map/subset-map.service';
import { SubsetNetworksPageComponent } from './networks/subset-networks-page.component';
import { SubsetOrphanNodesPageComponent } from './orphan-nodes/subset-orphan-nodes-page.component';
import { SubsetOrphanNodesService } from './orphan-nodes/components/subset-orphan-nodes.service';
import { SubsetOrphanRoutesPageComponent } from './orphan-routes/subset-orphan-routes-page.component';
import { SubsetOrphanRoutesService } from './orphan-routes/components/subset-orphan-routes.service';
import { SubsetEffects } from './store/subset.effects';
import { subsetReducer } from './store/subset.reducer';
import { subsetFeatureKey } from './store/subset.state';

export const subsetRoutes: Routes = [
  {
    path: '',
    providers: [
      provideState({
        name: subsetFeatureKey,
        reducer: subsetReducer,
      }),
      provideEffects([SubsetEffects]),
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
