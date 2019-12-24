import {CommonModule} from "@angular/common";
import {NgModule} from "@angular/core";
import {MatCheckboxModule} from "@angular/material";
import {MapClickService} from "./map-click.service";
import {MapComponent} from "./map.component";
import {MapService} from "./map.service";
import {NodeMapComponent} from "./node-map.component";
import {PoiTileLayerService} from "./poi-tile-layer.service";
import {RouteMapComponent} from "./route-map.component";
import {NodeMovedMapComponent} from "./node-moved-map.component";
import { SubsetMapComponent } from './subset-map.component';
import { LayerSwitcherComponent } from './layer-switcher.component';
import {TileLoadProgressService} from "./tile-load-progress.service";

@NgModule({
  imports: [
    CommonModule,
    MatCheckboxModule
  ],
  declarations: [
    MapComponent,
    NodeMapComponent,
    NodeMovedMapComponent,
    RouteMapComponent,
    SubsetMapComponent,
    LayerSwitcherComponent
  ],
  exports: [
    MapComponent,
    NodeMapComponent,
    NodeMovedMapComponent,
    RouteMapComponent,
    SubsetMapComponent
  ],
  providers: [
    MapService,
    MapClickService,
    PoiTileLayerService,
    TileLoadProgressService
  ]
})
export class OlModule {
}
