import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatDialogModule } from '@angular/material/dialog';
import { MatDividerModule } from '@angular/material/divider';
import { MatIconModule } from '@angular/material/icon';
import { MatMenuModule } from '@angular/material/menu';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { SharedModule } from '../shared';
import { LayerSwitcherComponent } from './components';
import { LegHttpErrorDialogComponent } from './components';
import { LegNotFoundDialogComponent } from './components';
import { MapLinkMenuComponent } from './components';
import { NoRouteDialogComponent } from './components';
import { PoiDetailMapComponent } from './components';
import { RouteControlComponent } from './components';
import { NewMapService } from './services';
import { MapClickService } from './services';
import { MapLayerTranslationService } from './services';
import { MapService } from './services';
import { PoiTileLayerService } from './services';

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
