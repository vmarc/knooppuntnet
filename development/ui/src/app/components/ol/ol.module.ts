import {CommonModule} from "@angular/common";
import {NgModule} from "@angular/core";
import {MapComponent} from "./map.component";
import {MapService} from "./map.service";
import {PoiTileLayerService} from "./poi-tile-layer.service";

@NgModule({
  imports: [
    CommonModule
  ],
  declarations: [
    MapComponent
  ],
  exports: [
    MapComponent
  ],
  providers: [
    MapService,
    PoiTileLayerService
  ]
})
export class OlModule {
}
