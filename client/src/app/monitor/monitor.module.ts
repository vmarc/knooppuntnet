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
import {MonitorAboutComponent} from './about/monitor-about.component';
import {MonitorChangesComponent} from './changes/monitor-changes.component';
import {MonitorGroupChangesComponent} from './group/changes/monitor-group-changes.component';
import {MonitorGroupComponent} from './group/details/monitor-group.component';
import {MonitorGroupsComponent} from './groups/monitor-groups.component';
import {MonitorRoutingModule} from './monitor-routing.module';
import {MonitorRouteChangeHeaderComponent} from './route/changes/monitor-route-change-header.component';
import {MonitorRouteChangeMapComponent} from './route/changes/monitor-route-change-map.component';
import {MonitorRouteChangePageComponent} from './route/changes/monitor-route-change-page.component';
import {MonitorRouteChangesComponent} from './route/changes/monitor-route-changes.component';
import {MonitorRoutePageHeaderComponent} from './route/components/monitor-route-page-header.component';
import {MonitorRouteDetailsComponent} from './route/details/monitor-route-details.component';
import {LegendLineComponent} from './route/map/legend-line';
import {MonitorRouteMapControlComponent} from './route/map/monitor-route-map-control.component';
import {MonitorRouteMapLayersComponent} from './route/map/monitor-route-map-layers.component';
import {MonitorRouteMapNokSegmentsComponent} from './route/map/monitor-route-map-nok-segments.component';
import {MonitorRouteMapOsmSegmentsComponent} from './route/map/monitor-route-map-osm-segments.component';
import {MonitorRouteMapSidebarComponent} from './route/map/monitor-route-map-sidebar.component';
import {MonitorRouteMapComponent} from './route/map/monitor-route-map.component';
import {MonitorRoutesTableComponent} from './routes/monitor-routes-table.component';
import {MonitorRoutesComponent} from './routes/monitor-routes.component';

@NgModule({
  imports: [
    CommonModule,
    MonitorRoutingModule,
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
    MonitorRoutesComponent,
    MonitorRoutesTableComponent,
    MonitorRouteDetailsComponent,
    MonitorRouteMapComponent,
    MonitorRouteChangesComponent,
    MonitorRoutePageHeaderComponent,
    MonitorRouteMapSidebarComponent,
    MonitorRouteMapOsmSegmentsComponent,
    MonitorRouteMapNokSegmentsComponent,
    LegendLineComponent,
    MonitorRouteMapControlComponent,
    MonitorRouteMapLayersComponent,
    MonitorRouteChangePageComponent,
    MonitorRouteChangeHeaderComponent,
    MonitorRouteChangeMapComponent,
    MonitorAboutComponent,
    MonitorChangesComponent,
    MonitorGroupChangesComponent,
    MonitorGroupComponent,
    MonitorGroupsComponent
  ],
  exports: [],
})
export class MonitorModule {
}
