import {CommonModule} from "@angular/common";
import {NgModule} from "@angular/core";
import {MatCheckboxModule} from "@angular/material/checkbox";
import {MatProgressBarModule} from "@angular/material/progress-bar";
import {SharedModule} from "../shared/shared.module";
import {LayerSwitcherComponent} from "./components/layer-switcher.component";
import {LocationMapComponent} from "./components/location-map.component";
import {NetworkMapComponent} from "./components/network-map.component";
import {NoRouteDialogComponent} from "./components/no-route-dialog.component";
import {NodeMapComponent} from "./components/node-map.component";
import {NodeMovedMapComponent} from "./components/node-moved-map.component";
import {RouteChangeMapComponent} from "./components/route-change-map.component";
import {RouteControlComponent} from "./components/route-control.component";
import {RouteMapComponent} from "./components/route-map.component";
import {SubsetMapComponent} from "./components/subset-map.component";
import {MapClickService} from "./services/map-click.service";
import {MapLayerService} from "./services/map-layer.service";
import {MapPositionService} from "./services/map-position.service";
import {MapService} from "./services/map.service";
import {PoiTileLayerService} from "./services/poi-tile-layer.service";
import {MatDialogModule} from "@angular/material/dialog";
import {PoiMapComponent} from "./components/poi-map.component";
import {LegNotFoundDialogComponent} from "./components/leg-not-found-dialog";
import {LegHttpErrorDialogComponent} from "./components/leg-http-error.dialog";

@NgModule({
  imports: [
    CommonModule,
    MatCheckboxModule,
    MatProgressBarModule,
    SharedModule,
    MatDialogModule
  ],
  declarations: [
    NodeMapComponent,
    NodeMovedMapComponent,
    RouteMapComponent,
    SubsetMapComponent,
    LayerSwitcherComponent,
    RouteChangeMapComponent,
    LocationMapComponent,
    NetworkMapComponent,
    RouteControlComponent,
    NoRouteDialogComponent,
    LegNotFoundDialogComponent,
    LegHttpErrorDialogComponent,
    PoiMapComponent
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
    PoiMapComponent
  ],
  providers: [
    MapService,
    MapLayerService,
    MapClickService,
    PoiTileLayerService,
    MapPositionService
  ]
})
export class OlModule {
}
