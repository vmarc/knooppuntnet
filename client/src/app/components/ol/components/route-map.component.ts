import {AfterViewInit, Component, Input, OnInit} from "@angular/core";
import {List} from "immutable";
import {Extent} from "ol/extent";
import Map from "ol/Map";
import View from "ol/View";
import {RouteInfo} from "../../../kpn/api/common/route/route-info";
import {Util} from "../../shared/util";
import {ZoomLevel} from "../domain/zoom-level";
import {MapControls} from "../layers/map-controls";
import {MapLayer} from "../layers/map-layer";
import {MapLayers} from "../layers/map-layers";
import {MapClickService} from "../services/map-click.service";
import {MapLayerService} from "../services/map-layer.service";
import {MapService} from "../services/map.service";

@Component({
  selector: "kpn-route-map",
  template: `
    <div id="route-map" class="kpn-map">
      <kpn-layer-switcher [mapLayers]="layers"></kpn-layer-switcher>
    </div>
  `
})
export class RouteMapComponent implements OnInit, AfterViewInit {

  @Input() routeInfo: RouteInfo;

  layers: MapLayers;
  private map: Map;

  constructor(private mapService: MapService,
              private mapClickService: MapClickService,
              private mapLayerService: MapLayerService) {
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

    this.layers.applyMap(this.map);
    this.mapClickService.installOn(this.map);

    const view = this.map.getView();
    view.fit(this.buildExtent());
  }

  private buildLayers(): MapLayers {
    let mapLayers: List<MapLayer> = List();
    mapLayers = mapLayers.push(this.mapLayerService.osmLayer());
    mapLayers = mapLayers.push(this.mapLayerService.networkLayer(this.routeInfo.summary.networkType));
    mapLayers = mapLayers.concat(this.mapLayerService.routeLayers(this.routeInfo.analysis.map));
    return new MapLayers(mapLayers);
  }

  private buildExtent(): Extent {
    const bounds = this.routeInfo.analysis.map.bounds;
    const min = Util.toCoordinate(bounds.latMin, bounds.lonMin);
    const max = Util.toCoordinate(bounds.latMax, bounds.lonMax);
    return [min[0], min[1], max[0], max[1]];
  }

}
