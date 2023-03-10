import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatDialogModule } from '@angular/material/dialog';
import { MatIconModule } from '@angular/material/icon';
import { MatMenuModule } from '@angular/material/menu';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { SharedModule } from '../shared/shared.module';
import { LayerSwitcherComponent } from './components/layer-switcher.component';
import { LegHttpErrorDialogComponent } from './components/leg-http-error.dialog';
import { LegNotFoundDialogComponent } from './components/leg-not-found-dialog';
import { LocationMapComponent } from './components/location-map.component';
import { MapLinkMenuComponent } from './components/map-link-menu.component';
import { NetworkControlComponent } from './components/network-control.component';
import { NetworkMapComponent } from './components/network-map.component';
import { NoRouteDialogComponent } from './components/no-route-dialog.component';
import { NodeMapComponent } from './components/node-map.component';
import { NodeMovedMapComponent } from './components/node-moved-map.component';
import { PoiDetailMapComponent } from './components/poi-detail-map.component';
import { PoiMapComponent } from './components/poi-map.component';
import { RouteChangeMapComponent } from './components/route-change-map.component';
import { RouteControlComponent } from './components/route-control.component';
import { RouteMapComponent } from './components/route-map.component';
import { SubsetMapComponent } from './components/subset-map.component';
import { MainMapPositionService } from './services/main-map-position.service';
import { MapClickService } from './services/map-click.service';
import { MapLayerService } from './services/map-layer.service';
import { MapPositionService } from './services/map-position.service';
import { MapService } from './services/map.service';
import { NetworkMapPositionService } from './services/network-map-position.service';
import { PoiTileLayerService } from './services/poi-tile-layer.service';
import { FrisoMapComponent } from '@app/components/ol/components/friso-map.component';

@NgModule({
  imports: [
    CommonModule,
    MatCheckboxModule,
    MatProgressBarModule,
    SharedModule,
    MatDialogModule,
    MatIconModule,
    MatMenuModule,
  ],
  declarations: [
    NodeMapComponent,
    NodeMovedMapComponent,
    RouteMapComponent,
    SubsetMapComponent,
    LayerSwitcherComponent,
    MapLinkMenuComponent,
    RouteChangeMapComponent,
    LocationMapComponent,
    NetworkMapComponent,
    RouteControlComponent,
    NetworkControlComponent,
    NoRouteDialogComponent,
    LegNotFoundDialogComponent,
    LegHttpErrorDialogComponent,
    PoiMapComponent,
    PoiDetailMapComponent,
    FrisoMapComponent,
  ],
  exports: [
    NodeMapComponent,
    NodeMovedMapComponent,
    RouteMapComponent,
    SubsetMapComponent,
    RouteChangeMapComponent,
    LocationMapComponent,
    LayerSwitcherComponent,
    NetworkMapComponent,
    RouteControlComponent,
    PoiMapComponent,
    PoiDetailMapComponent,
    MapLinkMenuComponent,
    FrisoMapComponent,
  ],
  providers: [
    MapService,
    MapLayerService,
    MapClickService,
    PoiTileLayerService,
    MapPositionService,
    MainMapPositionService,
    NetworkMapPositionService,
  ],
})
export class OlModule {}
