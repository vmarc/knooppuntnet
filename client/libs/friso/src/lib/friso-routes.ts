import { Routes } from '@angular/router';
import { PoiDetailMapService } from '@app/components/ol/components';
import { MapLayerTranslationService } from '@app/components/ol/services';
import { Util } from '@app/components/shared';
import { provideEffects } from '@ngrx/effects';
import { provideState } from '@ngrx/store';
import { FrisoMapService } from './friso/friso-map.service';
import { FrisoPageComponent } from './friso/friso-page.component';
import { FrisoSidebarComponent } from './friso/friso-sidebar.component';
import { FrisoEffects } from './store/friso.effects';
import { frisoReducer } from './store/friso.reducer';
import { frisoFeatureKey } from './store/friso.state';

export const frisoRoutes: Routes = [
  {
    path: '',
    providers: [
      MapLayerTranslationService,
      PoiDetailMapService,
      FrisoMapService,
      provideState({
        name: frisoFeatureKey,
        reducer: frisoReducer,
      }),
      provideEffects([FrisoEffects]),
    ],
    children: [Util.routePath('', FrisoPageComponent, FrisoSidebarComponent)],
  },
];
