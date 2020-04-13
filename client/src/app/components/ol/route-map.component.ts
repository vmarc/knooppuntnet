import {AfterViewInit, Component, Input, OnInit} from "@angular/core";
import {List} from "immutable";
import {Extent} from "ol/extent";
import BaseLayer from "ol/layer/Base";
import VectorTileLayer from "ol/layer/VectorTile";
import Map from "ol/Map";
import View from "ol/View";
import {I18nService} from "../../i18n/i18n.service";
import {RouteInfo} from "../../kpn/api/common/route/route-info";
import {Util} from "../shared/util";
import {MainMapStyle} from "./domain/main-map-style";
import {NodeMapStyle} from "./domain/node-map-style";
import {ZoomLevel} from "./domain/zoom-level";
import {MapControls} from "./layers/map-controls";
import {NetworkVectorTileLayer} from "./layers/network-vector-tile-layer";
import {MapClickService} from "./map-click.service";
import {MapLayerService} from "./map-layer.service";
import {MapService} from "./map.service";

@Component({
  selector: "kpn-route-map",
  template: `
    <div id="route-map" class="map">
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
export class RouteMapComponent implements OnInit, AfterViewInit {

  @Input() routeInfo: RouteInfo;

  layers: List<BaseLayer> = List();

  private map: Map;
  private vectorTileLayer: VectorTileLayer;

  constructor(private mapClickService: MapClickService,
              private mapService: MapService,
              private mapLayerService: MapLayerService,
              private i18nService: I18nService) {
  }

  ngOnInit(): void {
    this.layers = this.buildLayers();
  }

  ngAfterViewInit(): void {

    this.map = new Map({
      target: "route-map",
      layers: this.layers.toArray(),
      controls: MapControls.build(),
      view: new View({
        minZoom: ZoomLevel.minZoom,
        maxZoom: ZoomLevel.maxZoom
      })
    });

    this.mapClickService.installOn(this.map);

    const view = this.map.getView();
    view.fit(this.buildExtent());

    const nodeMapStyle = new NodeMapStyle(this.map).styleFunction();
    this.vectorTileLayer.setStyle(nodeMapStyle);
  }

  private buildLayers(): List<BaseLayer> {

    this.vectorTileLayer = this.buildVectorTileLayer();
    this.vectorTileLayer.set("name", this.i18nService.translation("@@map.layer.other-routes"));
    this.vectorTileLayer.setStyle(new MainMapStyle(this.map, this.mapService).styleFunction());

    return List([
      this.mapLayerService.osmLayer(),
      this.vectorTileLayer
    ])
      .concat(this.mapLayerService.routeLayers(this.routeInfo.analysis.map));
  }

  private buildExtent(): Extent {
    const bounds = this.routeInfo.analysis.map.bounds;
    const min = Util.toCoordinate(bounds.latMin, bounds.lonMin);
    const max = Util.toCoordinate(bounds.latMax, bounds.lonMax);
    return [min[0], min[1], max[0], max[1]];
  }

  private buildVectorTileLayer(): VectorTileLayer {
    return NetworkVectorTileLayer.build(this.routeInfo.summary.networkType);
  }

}
