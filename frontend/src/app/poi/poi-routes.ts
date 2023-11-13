import { Routes } from '@angular/router';
import { provideEffects } from '@ngrx/effects';
import { provideState } from '@ngrx/store';
import { PoiAreasPageComponent } from './areas/poi-areas-page.component';
import { PoiMapService } from './areas/poi-map.service';
import { PoiDetailPageComponent } from './detail/poi-detail-page.component';
import { PoiLocationPoisPageComponent } from './list/poi-location-pois-page.component';
import { PoiService } from './poi.service';
import { PoiEffects } from './store/poi.effects';
import { poiReducer } from './store/poi.reducer';
import { poiFeatureKey } from './store/poi.state';

export const poiRoutes: Routes = [
  {
    path: '',
    providers: [
      provideState({
        name: poiFeatureKey,
        reducer: poiReducer,
      }),
      provideEffects([PoiEffects]),
      PoiMapService,
      PoiService,
    ],
    children: [
      {
        path: 'areas',
        component: PoiAreasPageComponent,
      },
      {
        path: ':elementType/:elementId',
        component: PoiDetailPageComponent,
      },
      {
        path: 'location/:location/pois',
        component: PoiLocationPoisPageComponent,
      },
    ],
  },
];
