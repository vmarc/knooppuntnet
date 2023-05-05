import { Routes } from '@angular/router';
import { BaseSidebarComponent } from '@app/base';
import { Util } from '@app/components/shared';
import { provideEffects } from '@ngrx/effects';
import { provideState } from '@ngrx/store';
import { PoiAreasPageComponent } from './areas/poi-areas-page.component';
import { PoiMapService } from './areas/poi-map.service';
import { PoiDetailPageComponent } from './detail/poi-detail-page.component';
import { PoiLocationPoisPageComponent } from './list/poi-location-pois-page.component';
import { LocationPoisSidebarComponent } from './list/poi-location-pois-sidebar.component';
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
      PoiService,
      PoiMapService,
    ],
    children: [
      Util.routePath('areas', PoiAreasPageComponent, BaseSidebarComponent),
      Util.routePath(
        ':elementType/:elementId',
        PoiDetailPageComponent,
        BaseSidebarComponent
      ),
      Util.routePath(
        'location/:location/pois',
        PoiLocationPoisPageComponent,
        LocationPoisSidebarComponent
      ),
    ],
  },
];
