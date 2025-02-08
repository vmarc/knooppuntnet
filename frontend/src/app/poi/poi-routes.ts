import { Routes } from '@angular/router';
import { PoiMapService } from './internal/areas/components/poi-map.service';
import { PoiAreasPageComponent } from './internal/areas/poi-areas-page.component';
import { PoiDetailPageComponent } from './internal/detail/poi-detail-page.component';
import { PoiLocationPoisPageComponent } from './internal/list/poi-location-pois-page.component';
import { PoiService } from './internal/poi.service';

export const poiRoutes: Routes = [
  {
    path: '',
    providers: [PoiMapService, PoiService],
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
        path: 'location',
        component: PoiLocationPoisPageComponent,
      },
    ],
  },
];
