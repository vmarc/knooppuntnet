import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatDialogModule } from '@angular/material/dialog';
import { MatIconModule } from '@angular/material/icon';
import { MatMenuModule } from '@angular/material/menu';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { LayerSwitcherComponent } from '@app/components/ol/components/layer-switcher.component';
import { SharedModule } from '../shared/shared.module';
import { LegHttpErrorDialogComponent } from './components/leg-http-error.dialog';
import { LegNotFoundDialogComponent } from './components/leg-not-found-dialog';
import { MapLinkMenuComponent } from './components/map-link-menu.component';
import { NoRouteDialogComponent } from './components/no-route-dialog.component';
import { OldLayerSwitcherComponent } from './components/old-layer-switcher.component';
import { PoiDetailMapComponent } from './components/poi-detail-map.component';
import { PoiMapComponent } from './components/poi-map.component';
import { RouteControlComponent } from './components/route-control.component';
import { MapClickService } from './services/map-click.service';
import { MapLayerService } from './services/map-layer.service';
import { MapService } from './services/map.service';
import { NetworkMapPositionService } from './services/network-map-position.service';
import { OldMapPositionService } from './services/old-map-position.service';
import { PoiTileLayerService } from './services/poi-tile-layer.service';
import { NewMapService } from '@app/components/ol/services/new-map.service';
import { OldMapLinkMenuComponent } from '@app/components/ol/components/old-map-link-menu.component';
import { MatDividerModule } from '@angular/material/divider';

@NgModule({
  imports: [
    CommonModule,
    MatCheckboxModule,
    MatProgressBarModule,
    SharedModule,
    MatDialogModule,
    MatIconModule,
    MatMenuModule,
    MatDividerModule,
  ],
  declarations: [
    LayerSwitcherComponent,
    OldLayerSwitcherComponent,
    MapLinkMenuComponent,
    RouteControlComponent,
    NoRouteDialogComponent,
    LegNotFoundDialogComponent,
    LegHttpErrorDialogComponent,
    PoiMapComponent,
    PoiDetailMapComponent,
    OldMapLinkMenuComponent,
  ],
  exports: [
    OldLayerSwitcherComponent,
    RouteControlComponent,
    PoiMapComponent,
    PoiDetailMapComponent,
    MapLinkMenuComponent,
    LayerSwitcherComponent,
    OldMapLinkMenuComponent,
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
