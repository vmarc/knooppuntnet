import {CommonModule} from '@angular/common';
import {NgModule} from '@angular/core';
import {MatButtonModule} from '@angular/material/button';
import {MatCheckboxModule} from '@angular/material/checkbox';
import {MatIconModule} from '@angular/material/icon';
import {MatListModule} from '@angular/material/list';
import {MatRadioModule} from '@angular/material/radio';
import {MatSlideToggleModule} from '@angular/material/slide-toggle';
import {MatTableModule} from '@angular/material/table';
import {OlModule} from '../components/ol/ol.module';
import {SharedModule} from '../components/shared/shared.module';
import {LongDistanceExampleComponent} from './example/long-distance-example.component';
import {LongDistanceRoutingModule} from './long-distance-routing.module';
import {LegendLineComponent} from './route/legend-line';
import {LongDistanceRouteChangeHeaderComponent} from './route/long-distance-route-change-header.component';
import {LongDistanceRouteChangePageComponent} from './route/long-distance-route-change-page.component';
import {LongDistanceRouteChangesComponent} from './route/long-distance-route-changes.component';
import {LongDistanceRouteDetailsComponent} from './route/long-distance-route-details.component';
import {LongDistanceRouteMapControlComponent} from './route/long-distance-route-map-control.component';
import {LongDistanceRouteMapLayersComponent} from './route/long-distance-route-map-layers.component';
import {LongDistanceRouteMapNokSegmentsComponent} from './route/long-distance-route-map-nok-segments.component';
import {LongDistanceRouteMapOsmSegmentsComponent} from './route/long-distance-route-map-osm-segments.component';
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
    MatListModule,
    MatRadioModule,
    MatCheckboxModule,
    MatSlideToggleModule,
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
    LongDistanceRouteMapOsmSegmentsComponent,
    LongDistanceRouteMapNokSegmentsComponent,
    LegendLineComponent,
    LongDistanceRouteMapControlComponent,
    LongDistanceRouteMapLayersComponent,
    LongDistanceRouteChangePageComponent,
    LongDistanceRouteChangeHeaderComponent,
  ],
  exports: [],
})
export class LongDistanceModule {
}
