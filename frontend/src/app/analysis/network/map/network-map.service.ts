import { Injectable } from '@angular/core';
import { NetworkMapPage } from '@api/common/network';
import { NetworkMapPosition } from '@app/ol/domain';
import { ZoomLevel } from '@app/ol/domain';
import { BackgroundLayer } from '@app/ol/layers';
import { MapControls } from '@app/ol/layers';
import { MapLayerRegistry } from '@app/ol/layers';
import { NetworkNodesBitmapTileLayer } from '@app/ol/layers';
import { NetworkNodesMarkerLayer } from '@app/ol/layers';
import { NetworkNodesVectorTileLayer } from '@app/ol/layers';
import { OsmLayer } from '@app/ol/layers';
import { TileDebug256Layer } from '@app/ol/layers';
import { MapClickService } from '@app/ol/services';
import { MapZoomService } from '@app/ol/services';
import { OpenlayersMapService } from '@app/ol/services';
import { Util } from '@app/components/shared';
import { BrowserStorageService } from '@app/services';
import { Coordinate } from 'ol/coordinate';
import Map from 'ol/Map';
import View from 'ol/View';

@Injectable()
export class NetworkMapService extends OpenlayersMapService {
  private networkMapPositionKey = 'network-map-position';

  constructor(
    private mapZoomService: MapZoomService,
    private mapClickService: MapClickService,
    private storage: BrowserStorageService
  ) {
    super();
  }

  init(networkId: number, page: NetworkMapPage, mapPositionFromUrl: NetworkMapPosition): void {
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

    if (mapPositionFromUrl) {
      this.gotoLastKnownPosition(mapPositionFromUrl);
    } else {
      const mapPositionString = this.storage.get(this.networkMapPositionKey);
      if (mapPositionString == null) {
        view.fit(Util.toExtent(page.bounds, 0.1));
      } else {
        const mapPosition: NetworkMapPosition = JSON.parse(mapPositionString);
        if (networkId === mapPosition.networkId) {
          this.gotoLastKnownPosition(mapPosition);
        } else {
          view.fit(Util.toExtent(page.bounds, 0.1));
        }
      }
    }

    this.mapPosition$.subscribe((mapPosition) => {
      if (mapPosition) {
        const networkMapPosition: NetworkMapPosition = {
          networkId: networkId,
          zoom: mapPosition.zoom,
          x: mapPosition.x,
          y: mapPosition.y,
          rotation: mapPosition.rotation,
        };
        this.storage.set(this.networkMapPositionKey, JSON.stringify(networkMapPosition));
      }
    });

    this.mapZoomService.install(view);
    this.mapClickService.installOn(this.map);

    this.finalizeSetup(true);
  }

  private registerLayers(page: NetworkMapPage): void {
    const registry = new MapLayerRegistry();
    registry.register([], BackgroundLayer.build(), true);
    registry.register([], OsmLayer.build(), false);
    const networkNodesLayers = [
      NetworkNodesBitmapTileLayer.build(page.summary.networkType),
      NetworkNodesVectorTileLayer.build(page.summary.networkType, page.nodeIds, page.routeIds),
    ];
    registry.registerAll([], networkNodesLayers, true);
    registry.register([], NetworkNodesMarkerLayer.build(page.nodes), true);
    registry.register([], TileDebug256Layer.build(), false);
    this.register(registry);
  }

  private gotoLastKnownPosition(mapPosition: NetworkMapPosition): void {
    this.map.getView().setZoom(mapPosition.zoom);
    this.map.getView().setRotation(mapPosition.rotation);
    const center: Coordinate = [mapPosition.x, mapPosition.y];
    this.map.getView().setCenter(center);
  }
}
