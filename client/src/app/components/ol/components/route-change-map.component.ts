import {ChangeDetectionStrategy} from '@angular/core';
import {AfterViewInit} from '@angular/core';
import {Input} from '@angular/core';
import {Component} from '@angular/core';
import {List} from 'immutable';
import Map from 'ol/Map';
import View from 'ol/View';
import {Bounds} from '../../../kpn/api/common/bounds';
import {RawNode} from '../../../kpn/api/common/data/raw/raw-node';
import {GeometryDiff} from '../../../kpn/api/common/route/geometry-diff';
import {UniqueId} from '../../../kpn/common/unique-id';
import {Util} from '../../shared/util';
import {ZoomLevel} from '../domain/zoom-level';
import {MapControls} from '../layers/map-controls';
import {MapLayer} from '../layers/map-layer';
import {MapLayers} from '../layers/map-layers';
import {MapLayerService} from '../services/map-layer.service';
import {OnDestroy} from '@angular/core';

@Component({
  selector: 'kpn-route-change-map',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div [id]="mapId" class="kpn-embedded-map">
      <kpn-layer-switcher [mapLayers]="layers"></kpn-layer-switcher>
    </div>
  `
})
export class RouteChangeMapComponent implements AfterViewInit, OnDestroy {

  @Input() geometryDiff: GeometryDiff;
  @Input() nodes: List<RawNode>;
  @Input() bounds: Bounds;

  mapId = UniqueId.get();
  layers: MapLayers;
  private map: Map;

  constructor(private mapLayerService: MapLayerService) {
  }

  ngAfterViewInit(): void {
    setTimeout(() => this.buildMap(), 1);
  }

  ngOnDestroy(): void {
    if (this.map) {
      this.map.setTarget(null);
    }
  }

  buildMap(): void {
    this.layers = this.buildLayers();
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
    this.map.getView().fit(Util.toExtent(this.bounds, 0.1));
  }

  private buildLayers(): MapLayers {
    let mapLayers: List<MapLayer> = List();
    mapLayers = mapLayers.push(this.mapLayerService.backgroundLayer(this.mapId));
    mapLayers = mapLayers.push(this.mapLayerService.routeNodeLayer(this.nodes));
    mapLayers = mapLayers.concat(this.mapLayerService.routeChangeLayers(this.geometryDiff));
    return new MapLayers(mapLayers.filter(layer => layer !== null));
  }
}
