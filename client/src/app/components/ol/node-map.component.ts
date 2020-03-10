import {AfterViewInit, Component, Input} from "@angular/core";
import {Attribution, defaults as defaultControls} from "ol/control";
import TileLayer from "ol/layer/Tile";
import VectorLayer from "ol/layer/Vector";
import VectorTileLayer from "ol/layer/VectorTile";
import Map from "ol/Map";
import VectorSource from "ol/source/Vector";
import View from "ol/View";
import {NetworkType} from "../../kpn/api/custom/network-type";
import {NodeInfo} from "../../kpn/api/common/node-info";
import {Util} from "../shared/util";
import {Marker} from "./domain/marker";
import {NetworkBitmapTileLayer} from "./domain/network-bitmap-tile-layer";
import {NetworkVectorTileLayer} from "./domain/network-vector-tile-layer";
import {NodeMapStyle} from "./domain/node-map-style";
import {OsmLayer} from "./domain/osm-layer";
import {ZoomLevel} from "./domain/zoom-level";
import {MapClickService} from "./map-click.service";

@Component({
  selector: "kpn-node-map",
  template: `
    <div id="node-map" class="map"></div>
  `,
  styles: [`
    .map {
      position: absolute;
      top: 170px;
      left: 0;
      right: 0;
      bottom: 0;
    }
  `]
})
export class NodeMapComponent implements AfterViewInit {

  @Input() nodeInfo: NodeInfo;

  map: Map;

  bitmapTileLayer: TileLayer;
  vectorTileLayer: VectorTileLayer;

  constructor(private mapClickService: MapClickService) {
  }

  ngAfterViewInit(): void {

    this.bitmapTileLayer = NetworkBitmapTileLayer.build(NetworkType.hiking);
    this.vectorTileLayer = NetworkVectorTileLayer.build(NetworkType.hiking);

    const attribution = new Attribution({
      collapsible: false
    });

    const center = Util.toCoordinate(this.nodeInfo.latitude, this.nodeInfo.longitude);

    this.map = new Map({
      target: "node-map",
      layers: [
        OsmLayer.build(),
        this.bitmapTileLayer,
        this.vectorTileLayer,
        this.buildMarkerLayer()
      ],
      controls: defaultControls({attribution: false}).extend([attribution]),
      view: new View({
        center: center,
        minZoom: ZoomLevel.minZoom,
        maxZoom: ZoomLevel.maxZoom,
        zoom: 18
      })
    });

    this.mapClickService.installOn(this.map);

    const view = this.map.getView();
    view.on("change:resolution", () => this.zoom(view.getZoom()));

    const nodeMapStyle = new NodeMapStyle(this.map).styleFunction();
    this.vectorTileLayer.setStyle(nodeMapStyle);
    this.updateLayerVisibility(view.getZoom());
  }

  updateSize() {
    if (this.map != null) {
      this.map.updateSize();
    }
  }

  private zoom(zoomLevel: number) {
    this.updateLayerVisibility(zoomLevel);
    return true;
  }

  private buildMarkerLayer() {

    const coordinate = Util.toCoordinate(this.nodeInfo.latitude, this.nodeInfo.longitude);
    const marker = Marker.create("blue", coordinate);

    const source = new VectorSource();
    const layer = new VectorLayer({
      source: source
    });

    source.addFeature(marker);
    layer.set("name", "Node"); // TODO translate: "Knooppunt"
    return layer;
  }

  private updateLayerVisibility(zoomLevel: number) {
    const zoom = Math.round(zoomLevel);
    if (zoom <= ZoomLevel.bitmapTileMaxZoom) {
      this.bitmapTileLayer.setVisible(true);
      this.vectorTileLayer.setVisible(false);
    } else if (zoom >= ZoomLevel.vectorTileMinZoom) {
      this.bitmapTileLayer.setVisible(false);
      this.vectorTileLayer.setVisible(true);
    }
  }

}
