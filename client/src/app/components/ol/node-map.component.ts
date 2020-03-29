import {OnInit} from "@angular/core";
import {AfterViewInit, Component, Input} from "@angular/core";
import {List} from "immutable";
import {ScaleLine} from "ol/control";
import {FullScreen} from "ol/control";
import {Attribution, defaults as defaultControls} from "ol/control";
import BaseLayer from "ol/layer/Base";
import TileLayer from "ol/layer/Tile";
import VectorLayer from "ol/layer/Vector";
import VectorTileLayer from "ol/layer/VectorTile";
import Map from "ol/Map";
import VectorSource from "ol/source/Vector";
import View from "ol/View";
import {NodeInfo} from "../../kpn/api/common/node-info";
import {NetworkType} from "../../kpn/api/custom/network-type";
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
    <div id="node-map" class="map">
      <kpn-layer-switcher [layers]="layers"></kpn-layer-switcher>
    </div>
  `,
  styles: [`
    .map {
      position: absolute;
      top: 200px;
      left: 0;
      right: 0;
      bottom: 0;
      background-color: white;
    }
  `]
})
export class NodeMapComponent implements OnInit, AfterViewInit {

  @Input() nodeInfo: NodeInfo;

  map: Map;

  bitmapTileLayer: TileLayer;
  vectorTileLayer: VectorTileLayer;

  layers: List<BaseLayer> = List();

  constructor(private mapClickService: MapClickService) {
  }

  ngOnInit(): void {
    this.layers = this.buildLayers();
  }

  ngAfterViewInit(): void {


    const fullScreen = new FullScreen();
    const scaleLine = new ScaleLine();
    const attribution = new Attribution({
      collapsible: false
    });

    const center = Util.toCoordinate(this.nodeInfo.latitude, this.nodeInfo.longitude);

    this.map = new Map({
      target: "node-map",
      layers: this.layers.toArray(),
      controls: defaultControls({attribution: false}).extend([fullScreen, scaleLine, attribution]),
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

  private buildLayers(): List<BaseLayer> {

    this.bitmapTileLayer = NetworkBitmapTileLayer.build(NetworkType.hiking);
    this.vectorTileLayer = NetworkVectorTileLayer.build(NetworkType.hiking);

    this.bitmapTileLayer.set("name", "Hiking B"); // TODO translate
    this.vectorTileLayer.set("name", "Hiking V"); // TODO translate

    return List([
      OsmLayer.build(),
      this.bitmapTileLayer,
      this.vectorTileLayer,
      this.buildMarkerLayer()
    ]);
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

}
