import {CommonModule} from '@angular/common';
import {NgModule} from '@angular/core';
import {MatButtonModule} from '@angular/material/button';
import {MatIconModule} from '@angular/material/icon';
import {MatTableModule} from '@angular/material/table';
import {OlModule} from '../components/ol/ol.module';
import {SharedModule} from '../components/shared/shared.module';
import {LongDistanceExampleComponent} from './example/long-distance-example.component';
import {LongDistanceRoutingModule} from './long-distance-routing.module';
import {LongDistanceRouteChangesComponent} from './route/long-distance-route-changes.component';
import {LongDistanceRouteDetailsComponent} from './route/long-distance-route-details.component';
import {LongDistanceRouteMapLegendComponent} from './route/long-distance-route-map-legend.component';
import {LongDistanceRouteMapSegmentsComponent} from './route/long-distance-route-map-segments.component';
import {LongDistanceRouteMapSidebarComponent} from './route/long-distance-route-map-sidebar.component';
import {LongDistanceRouteMapComponent} from './route/long-distance-route-map.component';
import {LongDistanceRoutePageHeaderComponent} from './route/long-distance-route-page-header.component';
import {LongDistanceRoutesTableComponent} from './routes/long-distance-routes-table.component';
import {LongDistanceRoutesComponent} from './routes/long-distance-routes.component';

@NgModule({
  imports: [
    CommonModule,
    LongDistanceRoutingModule,
    SharedModule,
    OlModule,
    MatTableModule,
    MatIconModule,
    MatButtonModule,
  ],
  declarations: [
    LongDistanceRoutesComponent,
    LongDistanceRoutesTableComponent,
    LongDistanceRouteDetailsComponent,
    LongDistanceRouteMapComponent,
    LongDistanceRouteChangesComponent,
    LongDistanceExampleComponent,
    LongDistanceRoutePageHeaderComponent,
    LongDistanceRouteMapSidebarComponent,
    LongDistanceRouteMapLegendComponent,
    LongDistanceRouteMapSegmentsComponent,
  ],
  exports: [],
})
export class LongDistanceModule {
}
