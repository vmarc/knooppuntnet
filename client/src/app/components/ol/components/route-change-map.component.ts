import {AfterViewInit} from "@angular/core";
import {Input} from "@angular/core";
import {Component, OnInit} from "@angular/core";
import {List} from "immutable";
import {boundingExtent} from "ol/extent";
import Map from "ol/Map";
import {fromLonLat} from "ol/proj";
import View from "ol/View";
import {Bounds} from "../../../kpn/api/common/bounds";
import {RawNode} from "../../../kpn/api/common/data/raw/raw-node";
import {GeometryDiff} from "../../../kpn/api/common/route/geometry-diff";
import {UniqueId} from "../../../kpn/common/unique-id";
import {ZoomLevel} from "../domain/zoom-level";
import {MapControls} from "../layers/map-controls";
import {MapLayer} from "../layers/map-layer";
import {MapLayers} from "../layers/map-layers";
import {MapLayerService} from "../services/map-layer.service";

@Component({
  selector: "kpn-route-change-map",
  template: `
    <div [id]="mapId" class="kpn-embedded-map">
      <kpn-layer-switcher [mapLayers]="layers"></kpn-layer-switcher>
    </div>
  `
})
export class RouteChangeMapComponent implements OnInit, AfterViewInit {

  @Input() geometryDiff: GeometryDiff;
  @Input() nodes: List<RawNode>;
  @Input() bounds: Bounds;

  mapId = UniqueId.get();
  layers: MapLayers;
  private map: Map;

  constructor(private mapLayerService: MapLayerService) {
  }

  ngOnInit(): void {
    this.layers = this.buildLayers();
  }

  ngAfterViewInit(): void {
    setTimeout(() => this.buildMap(), 1);
  }

  buildMap(): void {
    this.map = new Map({
      target: this.mapId,
      layers: this.layers.toArray(),
      controls: MapControls.build(),
      view: new View({
        minZoom: ZoomLevel.vectorTileMinZoom,
        maxZoom: ZoomLevel.maxZoom
      })
    });
    this.layers.applyMap(this.map);
    this.fitBounds();
  }

  private buildLayers(): MapLayers {
    let mapLayers: List<MapLayer> = List();
    mapLayers = mapLayers.push(this.mapLayerService.osmLayer());
    mapLayers = mapLayers.push(this.mapLayerService.routeNodeLayer(this.nodes));
    mapLayers = mapLayers.concat(this.mapLayerService.routeChangeLayers(this.geometryDiff));
    return new MapLayers(mapLayers.filter(layer => layer !== null));
  }

  private fitBounds(): void {

    // note that the 'common' points are not taken into account here, so that we zoom in on the actual changes
    const points = this.geometryDiff.before.concat(this.geometryDiff.after);

    if (points.isEmpty()) {
      const southWest = fromLonLat([this.bounds.minLon, this.bounds.minLat]);
      const northEast = fromLonLat([this.bounds.maxLon, this.bounds.maxLat]);
      this.map.getView().fit(boundingExtent([southWest, northEast]));
    } else {
      const lattitudes = points.flatMap(s => List([s.p1.latitude, s.p2.latitude])).map(l => +l);
      const longitudes = points.flatMap(s => List([s.p1.longitude, s.p2.longitude])).map(l => +l);

      const latMin = lattitudes.min();
      const lonMin = longitudes.min();
      const latMax = lattitudes.max();
      const lonMax = longitudes.max();

      const latDelta = (latMax - latMin) / 8;
      const lonDelta = (lonMax - lonMin) / 8;

      const southWest = fromLonLat([lonMin - lonDelta, latMin - latDelta]);
      const northEast = fromLonLat([lonMax + lonDelta, latMax + latDelta]);

      this.map.getView().fit(boundingExtent([southWest, northEast]));
    }
  }
}
