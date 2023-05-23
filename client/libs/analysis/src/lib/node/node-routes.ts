import { Routes } from '@angular/router';
import { provideEffects } from '@ngrx/effects';
import { provideState } from '@ngrx/store';
import { NodeChangesPageComponent } from './changes/_node-changes-page.component';
import { NodeDetailsPageComponent } from './details/_node-details-page.component';
import { NodeMapPageComponent } from './map/_node-map-page.component';
import { NodeMapService } from './map/node-map.service';
import { NodeEffects } from './store/node.effects';
import { nodeReducer } from './store/node.reducer';
import { nodeFeatureKey } from './store/node.state';

export const nodeRoutes: Routes = [
  {
    path: '',
    providers: [
      provideState({
        name: nodeFeatureKey,
        reducer: nodeReducer,
      }),
      provideEffects([NodeEffects]),
      NodeMapService, // provided here: referenced in effects
    ],
    children: [
      {
        path: ':nodeId',
        component: NodeDetailsPageComponent,
      },
      {
        path: ':nodeId/map',
        component: NodeMapPageComponent,
      },
      {
        path: ':nodeId/changes',
        component: NodeChangesPageComponent,
      },
    ],
  },
];
