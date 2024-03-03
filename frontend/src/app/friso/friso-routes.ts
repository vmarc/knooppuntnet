import { Routes } from '@angular/router';
import { PoiDetailMapService } from '@app/ol/components';
import { provideEffects } from '@ngrx/effects';
import { provideState } from '@ngrx/store';
import { FrisoMapService } from './friso/friso-map.service';
import { FrisoPageComponent } from './friso/friso-page.component';
import { FrisoEffects } from './store/friso.effects';
import { frisoReducer } from './store/friso.reducer';
import { frisoFeatureKey } from './store/friso.state';

export const frisoRoutes: Routes = [
  {
    path: '',
    providers: [
      PoiDetailMapService,
      FrisoMapService,
      provideState({
        name: frisoFeatureKey,
        reducer: frisoReducer,
      }),
      provideEffects([FrisoEffects]),
    ],
    children: [
      {
        path: '',
        component: FrisoPageComponent,
      },
    ],
  },
];
