import { Routes } from '@angular/router';
import { GlobeComponent } from './maps/globe.component';
import { OsmRasterTilesComponent } from './maps/osm-raster-tiles.component';
import { OsmVectorTilesComponent } from './maps/osm-vector-tiles.component';

export const routes: Routes = [
  {
    path: 'globe',
    component: GlobeComponent,
  },
  {
    path: 'osm-raster-tiles',
    component: OsmRasterTilesComponent,
  },
  {
    path: 'osm-vector-tiles',
    component: OsmVectorTilesComponent,
  },
];
