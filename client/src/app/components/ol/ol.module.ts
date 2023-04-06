import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatDialogModule } from '@angular/material/dialog';
import { MatIconModule } from '@angular/material/icon';
import { MatMenuModule } from '@angular/material/menu';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { FrisoMapComponent } from '@app/components/ol/components/friso-map.component';
import { LayerSwitcherComponent } from '@app/components/ol/components/layer-switcher.component';
import { SharedModule } from '../shared/shared.module';
import { LegHttpErrorDialogComponent } from './components/leg-http-error.dialog';
import { LegNotFoundDialogComponent } from './components/leg-not-found-dialog';
import { MapLinkMenuComponent } from './components/map-link-menu.component';
import { NetworkControlComponent } from './components/network-control.component';
import { NetworkMapComponent } from './components/network-map.component';
import { NoRouteDialogComponent } from './components/no-route-dialog.component';
import { NodeMovedMapComponent } from './components/node-moved-map.component';
import { OldLayerSwitcherComponent } from './components/old-layer-switcher.component';
import { PoiDetailMapComponent } from './components/poi-detail-map.component';
import { PoiMapComponent } from './components/poi-map.component';
import { RouteChangeMapComponent } from './components/route-change-map.component';
import { RouteControlComponent } from './components/route-control.component';
import { RouteMapComponent } from './components/route-map.component';
import { SubsetMapComponent } from './components/subset-map.component';
import { MapClickService } from './services/map-click.service';
import { MapLayerService } from './services/map-layer.service';
import { MapService } from './services/map.service';
import { NetworkMapPositionService } from './services/network-map-position.service';
import { OldMapPositionService } from './services/old-map-position.service';
import { PoiTileLayerService } from './services/poi-tile-layer.service';
import { NewMapService } from '@app/components/ol/services/new-map.service';

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
    NodeMovedMapComponent,
    RouteMapComponent,
    SubsetMapComponent,
    LayerSwitcherComponent,
    OldLayerSwitcherComponent,
    MapLinkMenuComponent,
    RouteChangeMapComponent,
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
    NodeMovedMapComponent,
    RouteMapComponent,
    SubsetMapComponent,
    RouteChangeMapComponent,
    OldLayerSwitcherComponent,
    NetworkMapComponent,
    RouteControlComponent,
    PoiMapComponent,
    PoiDetailMapComponent,
    MapLinkMenuComponent,
    FrisoMapComponent,
    LayerSwitcherComponent,
  ],
  providers: [
    NewMapService,
    MapService,
    MapLayerService,
    MapClickService,
    PoiTileLayerService,
    OldMapPositionService,
    NetworkMapPositionService,
  ],
})
export class OlModule {}
