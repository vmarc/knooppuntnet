import { OpenlayersMapService } from '@app/components/ol/services/openlayers-map-service';
import { MapLayerRegistry } from '@app/components/ol/layers/map-layer-registry';
import { BackgroundLayer } from '@app/components/ol/layers/background-layer';
import { OsmLayer } from '@app/components/ol/layers/osm-layer';
import { NetworkNodesBitmapTileLayer } from '@app/components/ol/layers/network-nodes-bitmap-tile-layer';
import { NetworkNodesMarkerLayer } from '@app/components/ol/layers/network-nodes-marker-layer';
import { TileDebug256Layer } from '@app/components/ol/layers/tile-debug-256-layer';
import { NetworkMapPage } from '@api/common/network/network-map-page';
import { MapControls } from '@app/components/ol/layers/map-controls';
import View from 'ol/View';
import { ZoomLevel } from '@app/components/ol/domain/zoom-level';
import { NetworkMapPositionService } from '@app/components/ol/services/network-map-position.service';
import Map from 'ol/Map';
import { MapZoomService } from '@app/components/ol/services/map-zoom.service';
import { NetworkMapPosition } from '@app/components/ol/domain/network-map-position';
import { MapClickService } from '@app/components/ol/services/map-click.service';
import { Injectable } from '@angular/core';
import { NetworkNodesVectorTileLayer } from '@app/components/ol/layers/network-nodes-vector-tile-layer';

@Injectable()
export class NetworkMapService extends OpenlayersMapService {
  constructor(
    private networkMapPositionService: NetworkMapPositionService,
    private mapZoomService: MapZoomService,
    private mapClickService: MapClickService
  ) {
    super();
  }

  init(
    networkId: number,
    page: NetworkMapPage,
    mapPositionFromUrl: NetworkMapPosition
  ): void {
    this.registerLayers(page);

    this.initMap(
      new Map({
        target: this.mapId,
        layers: this.layers,
        controls: MapControls.build(),
        view: new View({
          minZoom: ZoomLevel.minZoom,
          maxZoom: ZoomLevel.maxZoom,
        }),
      })
    );

    const view = this.map.getView();
    this.networkMapPositionService.install(
      view,
      networkId,
      page.bounds,
      mapPositionFromUrl
    );
    this.mapZoomService.install(view);
    this.mapClickService.installOn(this.map);

    this.finalizeSetup();
  }

  private registerLayers(page: NetworkMapPage): void {
    const registry = new MapLayerRegistry();
    registry.register([], BackgroundLayer.build(), true);
    registry.register([], OsmLayer.build(), false);
    const networkNodesLayers = [
      NetworkNodesBitmapTileLayer.build(page.summary.networkType),
      NetworkNodesVectorTileLayer.build(
        page.summary.networkType,
        page.nodeIds,
        page.routeIds
      ),
    ];
    registry.registerAll([], networkNodesLayers, true);
    registry.register([], NetworkNodesMarkerLayer.build(page.nodes), true);
    registry.register([], TileDebug256Layer.build(), false);
    this.register(registry);
  }
}
