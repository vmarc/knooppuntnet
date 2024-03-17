import { Routes } from '@angular/router';
import { ChangesService } from '@app/analysis/components/changes/filter';
import { provideEffects } from '@ngrx/effects';
import { provideState } from '@ngrx/store';
import { ChangesPageComponent } from './changes-page.component';
import { ChangesEffects } from './store/changes.effects';
import { changesReducer } from './store/changes.reducer';
import { changesFeatureKey } from './store/changes.state';

export const changesRoutes: Routes = [
  {
    path: '',
    providers: [
      provideState({
        name: changesFeatureKey,
        reducer: changesReducer,
      }),
      provideEffects([ChangesEffects]),
      ChangesService,
    ],
    children: [
      {
        path: '',
        component: ChangesPageComponent,
      },
    ],
  },
];
