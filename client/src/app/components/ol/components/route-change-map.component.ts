import { ChangeDetectionStrategy } from '@angular/core';
import { AfterViewInit } from '@angular/core';
import { Input } from '@angular/core';
import { Component } from '@angular/core';
import { OnDestroy } from '@angular/core';
import { Bounds } from '@api/common/bounds';
import { RawNode } from '@api/common/data/raw/raw-node';
import { GeometryDiff } from '@api/common/route/geometry-diff';
import { List } from 'immutable';
import Map from 'ol/Map';
import View from 'ol/View';
import { fromEvent } from 'rxjs';
import { UniqueId } from '@app/kpn/common/unique-id';
import { Subscriptions } from '@app/util/Subscriptions';
import { Util } from '../../shared/util';
import { ZoomLevel } from '../domain/zoom-level';
import { MapControls } from '../layers/map-controls';
import { MapLayer } from '../layers/map-layer';
import { MapLayers } from '../layers/map-layers';
import { MapLayerService } from '../services/map-layer.service';
import { BackgroundLayer } from '@app/components/ol/layers/background-layer';
import { RouteNodesLayer } from '@app/components/ol/layers/route-nodes-layer';

@Component({
  selector: 'kpn-route-change-map',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div [id]="mapId" class="kpn-embedded-map">
      <kpn-old-layer-switcher [mapLayers]="layers" />
      <kpn-map-link-menu [map]="map" />
    </div>
  `,
})
export class RouteChangeMapComponent implements AfterViewInit, OnDestroy {
  @Input() geometryDiff: GeometryDiff;
  @Input() nodes: RawNode[];
  @Input() bounds: Bounds;
  private readonly subscriptions = new Subscriptions();

  mapId = UniqueId.get();
  map: Map;
  layers: MapLayers;

  constructor(private mapLayerService: MapLayerService) {}

  ngAfterViewInit(): void {
    setTimeout(() => this.buildMap(), 1);
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
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
        maxZoom: ZoomLevel.maxZoom,
      }),
    });
    this.map.getView().fit(Util.toExtent(this.bounds, 0.1));

    this.subscriptions.add(
      fromEvent(window, 'webkitfullscreenchange').subscribe(() =>
        this.updateSize()
      )
    );
  }

  private updateSize(): void {
    if (this.map) {
      setTimeout(() => {
        this.map.updateSize();
        this.layers.updateSize();
      }, 0);
    }
  }

  private buildLayers(): MapLayers {
    let mapLayers: List<MapLayer> = List();
    mapLayers = mapLayers.push(BackgroundLayer.build());
    mapLayers = mapLayers.push(new RouteNodesLayer().build(this.nodes));
    mapLayers = mapLayers.concat(
      this.mapLayerService.routeChangeLayers(this.geometryDiff)
    );
    return new MapLayers(mapLayers.filter((layer) => layer !== null));
  }
}
