import {OnInit} from "@angular/core";
import {AfterViewInit, Component, Input} from "@angular/core";
import {List} from "immutable";
import BaseLayer from "ol/layer/Base";
import LayerGroup from "ol/layer/Group";
import VectorTileLayer from "ol/layer/VectorTile";
import Map from "ol/Map";
import View from "ol/View";
import {NodeMapInfo} from "../../kpn/api/common/node-map-info";
import {Util} from "../shared/util";
import {NodeMapStyle} from "./domain/node-map-style";
import {ZoomLevel} from "./domain/zoom-level";
import {MapControls} from "./layers/map-controls";
import {MapClickService} from "./map-click.service";
import {MapLayerService} from "./map-layer.service";

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

  @Input() nodeMapInfo: NodeMapInfo;

  map: Map;
  layers: List<BaseLayer> = List();

  constructor(private mapClickService: MapClickService,
              private mapLayerService: MapLayerService) {
  }

  ngOnInit(): void {
    this.layers = this.buildLayers();
  }

  ngAfterViewInit(): void {

    const center = Util.toCoordinate(this.nodeMapInfo.latitude, this.nodeMapInfo.longitude);

    this.map = new Map({
      target: "node-map",
      layers: this.layers.toArray(),
      controls: MapControls.build(),
      view: new View({
        center: center,
        minZoom: ZoomLevel.vectorTileMinZoom,
        maxZoom: ZoomLevel.maxZoom,
        zoom: 18
      })
    });

    const nodeMapStyle = new NodeMapStyle(this.map).styleFunction();
    this.layers.filter(layer => layer instanceof VectorTileLayer && layer.get("mapStyle") === "nodeMapStyle").forEach(layer => {
      (layer as VectorTileLayer).setStyle(nodeMapStyle);
    });

    this.map.setLayerGroup(new LayerGroup({layers: this.layers.toArray()}));
    this.mapClickService.installOn(this.map);
  }

  private buildLayers(): List<BaseLayer> {
    return List([this.mapLayerService.osmLayer()])
      .concat(this.mapLayerService.networkLayers(this.nodeMapInfo.networkTypes))
      .concat(List([this.mapLayerService.nodeMarkerLayer(this.nodeMapInfo)]));
  }

}
