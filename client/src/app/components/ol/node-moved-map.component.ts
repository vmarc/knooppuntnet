import Feature from "ol/Feature";
import LineString from "ol/geom/LineString";
import Stroke from "ol/style/Stroke";
import Style from "ol/style/Style";
import {AfterViewInit, Component, Input} from "@angular/core";
import {Attribution, defaults as defaultControls} from "ol/control";
import VectorLayer from "ol/layer/Vector";
import Map from "ol/Map";
import VectorSource from "ol/source/Vector";
import View from "ol/View";
import {Util} from "../shared/util";
import {Marker} from "./domain/marker";
import {OsmLayer} from "./domain/osm-layer";
import {ZoomLevel} from "./domain/zoom-level";
import {NodeMoved} from "../../kpn/shared/diff/node/node-moved";
import {UniqueId} from "../../kpn/common/unique-id";

@Component({
  selector: "kpn-node-moved-map",
  template: `
    <div id="{{mapId}}" class="map"></div>
  `,
  styles: [`
    .map {
      width: 500px;
      height: 300px;
    }
  `]
})
export class NodeMovedMapComponent implements AfterViewInit {

  @Input() nodeMoved: NodeMoved;

  map: Map;
  mapId = UniqueId.get();

  ngAfterViewInit(): void {

    const attribution = new Attribution({
      collapsible: false
    });

    const center = Util.latLonToCoordinate(this.nodeMoved.after);

    this.map = new Map({
      declutter: true,
      target: this.mapId,
      layers: [
        OsmLayer.build(),
        this.buildDetailLayer()
      ],
      controls: defaultControls({attribution: false}).extend([attribution]),
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
