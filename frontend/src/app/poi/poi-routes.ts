import { Routes } from '@angular/router';
import { PoiMapService } from './areas/components/poi-map.service';
import { PoiAreasPageComponent } from './areas/poi-areas-page.component';
import { PoiDetailPageComponent } from './detail/poi-detail-page.component';
import { PoiLocationPoisPageComponent } from './list/poi-location-pois-page.component';
import { PoiService } from './poi.service';

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
