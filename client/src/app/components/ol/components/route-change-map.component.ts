import { ChangeDetectionStrategy } from '@angular/core';
import { AfterViewInit } from '@angular/core';
import { Input } from '@angular/core';
import { Component } from '@angular/core';
import { OnDestroy } from '@angular/core';
import { Bounds } from '@api/common/bounds';
import { RawNode } from '@api/common/data/raw/raw-node';
import { GeometryDiff } from '@api/common/route/geometry-diff';
import { List } from 'immutable';
import View from 'ol/View';
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
import { NewMapService } from '@app/components/ol/services/new-map.service';
import { OpenLayersMap } from '@app/components/ol/domain/open-layers-map';
import { OsmLayer } from '@app/components/ol/layers/osm-layer';

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

  protected map: OpenLayersMap;
  protected readonly mapId = UniqueId.get();
  protected layers: MapLayers;

  constructor(
    private newMapService: NewMapService,
    private mapLayerService: MapLayerService
  ) {}

  ngAfterViewInit(): void {
    setTimeout(() => this.buildMap(), 1);
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
    this.map.destroy();
  }

  buildMap(): void {
    this.layers = this.buildLayers();
    this.map = this.newMapService.build({
      target: this.mapId,
      layers: this.layers.toArray(),
      controls: MapControls.build(),
      view: new View({
        minZoom: ZoomLevel.vectorTileMinZoom,
        maxZoom: ZoomLevel.maxZoom,
      }),
    });
    this.map.map.getView().fit(Util.toExtent(this.bounds, 0.1));
  }

  private buildLayers(): MapLayers {
    let mapLayers: List<MapLayer> = List();
    mapLayers = mapLayers.push(BackgroundLayer.build());
    mapLayers = mapLayers.push(OsmLayer.build());
    mapLayers = mapLayers.push(new RouteNodesLayer().build(this.nodes));
    mapLayers = mapLayers.concat(
      this.mapLayerService.routeChangeLayers(this.geometryDiff)
    );
    return new MapLayers(mapLayers.filter((layer) => layer !== null));
  }
}
