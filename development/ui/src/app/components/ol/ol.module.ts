import {CommonModule} from "@angular/common";
import {NgModule} from "@angular/core";
import {MapComponent} from "./map.component";
import {MapService} from "./map.service";
import {NodeMapComponent} from "./node-map.component";
import {PoiTileLayerService} from "./poi-tile-layer.service";

@NgModule({
  imports: [
    CommonModule
  ],
  declarations: [
    MapComponent,
    NodeMapComponent
  ],
  exports: [
    MapComponent,
    NodeMapComponent
  ],
  providers: [
    MapService,
    PoiTileLayerService
  ]
})
export class OlModule {
}
