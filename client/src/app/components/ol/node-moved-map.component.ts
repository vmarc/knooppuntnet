import {AfterViewInit, Component, Input} from "@angular/core";
import Feature from "ol/Feature";
import LineString from "ol/geom/LineString";
import VectorLayer from "ol/layer/Vector";
import Map from "ol/Map";
import VectorSource from "ol/source/Vector";
import Stroke from "ol/style/Stroke";
import Style from "ol/style/Style";
import View from "ol/View";
import {NodeMoved} from "../../kpn/api/common/diff/node/node-moved";
import {UniqueId} from "../../kpn/common/unique-id";
import {Util} from "../shared/util";
import {Marker} from "./domain/marker";
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
    setTimeout(() => {
      this.buildMap();
    }, 100);
  }

  buildMap(): void {

    const center = Util.latLonToCoordinate(this.nodeMoved.after);

    this.map = new Map({
      target: this.mapId,
      layers: [
        this.mapLayerService.osmLayer(),
        this.buildDetailLayer()
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

  private buildDetailLayer() {

    const before = Util.latLonToCoordinate(this.nodeMoved.before);
    const after = Util.latLonToCoordinate(this.nodeMoved.after);
    const nodeMarker = Marker.create("blue", after);

    const displacement = new Feature(new LineString([before, after]));
    displacement.setStyle(new Style({
      stroke: new Stroke({
        color: "rgb(255, 0, 0)",
        width: 5
      })
    }));

    const source = new VectorSource();
    source.addFeature(displacement);
    source.addFeature(nodeMarker);
    return new VectorLayer({
      source: source
    });
  }

}
