import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatDialogModule } from '@angular/material/dialog';
import { MatDividerModule } from '@angular/material/divider';
import { MatIconModule } from '@angular/material/icon';
import { MatMenuModule } from '@angular/material/menu';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { LayerSwitcherComponent } from '@app/components/ol/components/layer-switcher.component';
import { NewMapService } from '@app/components/ol/services/new-map.service';
import { SharedModule } from '../shared/shared.module';
import { LegHttpErrorDialogComponent } from './components/leg-http-error.dialog';
import { LegNotFoundDialogComponent } from './components/leg-not-found-dialog';
import { MapLinkMenuComponent } from './components/map-link-menu.component';
import { NoRouteDialogComponent } from './components/no-route-dialog.component';
import { PoiDetailMapComponent } from './components/poi-detail-map.component';
import { RouteControlComponent } from './components/route-control.component';
import { MapClickService } from './services/map-click.service';
import { MapLayerTranslationService } from './services/map-layer-translation.service';
import { MapService } from './services/map.service';
import { PoiTileLayerService } from './services/poi-tile-layer.service';

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
    MapLinkMenuComponent,
    RouteControlComponent,
    NoRouteDialogComponent,
    LegNotFoundDialogComponent,
    LegHttpErrorDialogComponent,
    PoiDetailMapComponent,
  ],
  exports: [
    RouteControlComponent,
    PoiDetailMapComponent,
    MapLinkMenuComponent,
    LayerSwitcherComponent,
  ],
  providers: [
    NewMapService,
    MapService,
    MapLayerTranslationService,
    MapClickService,
    PoiTileLayerService,
  ],
})
export class OlModule {}
