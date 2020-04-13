import {AfterViewInit, Component, Input} from "@angular/core";
import Map from "ol/Map";
import View from "ol/View";
import {NodeMoved} from "../../kpn/api/common/diff/node/node-moved";
import {UniqueId} from "../../kpn/common/unique-id";
import {Util} from "../shared/util";
import {ZoomLevel} from "./domain/zoom-level";
import {MapControls} from "./layers/map-controls";
import {MapLayerService} from "./map-layer.service";

@Component({
  selector: "kpn-node-moved-map",
  template: `
    <div [id]="mapId" class="map"></div>
  `,
  styles: [`
    .map {
      position: relative;
      left: 0;
      right: 20px;
      height: 320px;
      max-width: 640px;
      border: 1px solid lightgray;
      background-color: white;
    }
  `]
})
export class NodeMovedMapComponent implements AfterViewInit {

  @Input() nodeMoved: NodeMoved;

  map: Map;
  mapId = UniqueId.get();

  constructor(private mapLayerService: MapLayerService) {
  }

  ngAfterViewInit(): void {
    setTimeout(() => this.buildMap(), 100);
  }

  buildMap(): void {

    const center = Util.latLonToCoordinate(this.nodeMoved.after);

    this.map = new Map({
      target: this.mapId,
      layers: [
        this.mapLayerService.osmLayer(),
        this.mapLayerService.nodeMovedLayer(this.nodeMoved)
      ],
      controls: MapControls.build(),
      view: new View({
        center: center,
        minZoom: ZoomLevel.minZoom,
        maxZoom: ZoomLevel.maxZoom,
        zoom: 18
      })
    });
  }

}
