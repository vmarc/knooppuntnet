import {CommonModule} from "@angular/common";
import {NgModule} from "@angular/core";
import {MatCheckboxModule} from "@angular/material/checkbox";
import {MatProgressBarModule} from "@angular/material/progress-bar";
import {LayerSwitcherComponent} from "./layer-switcher.component";
import {LocationMapComponent} from "./location-map.component";
import {MapClickService} from "./map-click.service";
import {MapLayerService} from "./map-layer.service";
import {MapPositionService} from "./map-position.service";
import {MapService} from "./map.service";
import {NodeMapComponent} from "./node-map.component";
import {NodeMovedMapComponent} from "./node-moved-map.component";
import {PoiTileLayerService} from "./poi-tile-layer.service";
import {RouteChangeMapComponent} from "./route-change-map.component";
import {RouteMapComponent} from "./route-map.component";
import {SubsetMapComponent} from "./subset-map.component";
import {TileLoadProgressService} from "./tile-load-progress.service";

@NgModule({
  imports: [
    CommonModule,
    MatCheckboxModule,
    MatProgressBarModule
  ],
  declarations: [
    NodeMapComponent,
    NodeMovedMapComponent,
    RouteMapComponent,
    SubsetMapComponent,
    LayerSwitcherComponent,
    RouteChangeMapComponent,
    LocationMapComponent
  ],
  exports: [
    NodeMapComponent,
    NodeMovedMapComponent,
    RouteMapComponent,
    SubsetMapComponent,
    RouteChangeMapComponent,
    LocationMapComponent
  ],
  providers: [
    MapService,
    MapLayerService,
    MapClickService,
    PoiTileLayerService,
    TileLoadProgressService,
    MapPositionService
  ]
})
export class OlModule {
}
