import {CommonModule} from "@angular/common";
import {NgModule} from "@angular/core";
import {MatCheckboxModule} from "@angular/material/checkbox";
import {LayerSwitcherComponent} from "./layer-switcher.component";
import {MapClickService} from "./map-click.service";
import {MapPositionService} from "./map-position.service";
import {MapService} from "./map.service";
import {NodeMapComponent} from "./node-map.component";
import {NodeMovedMapComponent} from "./node-moved-map.component";
import {PoiTileLayerService} from "./poi-tile-layer.service";
import {RouteMapComponent} from "./route-map.component";
import {SubsetMapComponent} from "./subset-map.component";
import {TileLoadProgressService} from "./tile-load-progress.service";

@NgModule({
  imports: [
    CommonModule,
    MatCheckboxModule
  ],
  declarations: [
    NodeMapComponent,
    NodeMovedMapComponent,
    RouteMapComponent,
    SubsetMapComponent,
    LayerSwitcherComponent
  ],
  exports: [
    NodeMapComponent,
    NodeMovedMapComponent,
    RouteMapComponent,
    SubsetMapComponent
  ],
  providers: [
    MapService,
    MapClickService,
    PoiTileLayerService,
    TileLoadProgressService,
    MapPositionService
  ]
})
export class OlModule {
}
