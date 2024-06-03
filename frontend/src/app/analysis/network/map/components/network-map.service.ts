import { effect } from '@angular/core';
import { inject } from '@angular/core';
import { Injectable } from '@angular/core';
import { NetworkMapPage } from '@api/common/network';
import { Util } from '@app/components/shared';
import { CachedMapPosition } from '@app/ol/domain';
import { ZoomLevel } from '@app/ol/domain';
import { OpenDataLayers } from '@app/ol/layers';
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
import { BrowserStorageService } from '@app/services';
import { Coordinate } from 'ol/coordinate';
import Map from 'ol/Map';
import View from 'ol/View';

@Injectable()
export class NetworkMapService extends OpenlayersMapService {
  private readonly mapZoomService = inject(MapZoomService);
  private readonly mapClickService = inject(MapClickService);
  private readonly storage = inject(BrowserStorageService);

  private networkMapPositionKey = 'network-map-position';

  networkId: number | null = null;

  constructor() {
    super();
    effect(() => {
      const mapPosition = this.mapPosition();
      if (mapPosition && this.networkId) {
        const networkMapPosition = mapPosition.toCachedMapPosition(this.networkId.toString());
        this.storage.set(this.networkMapPositionKey, JSON.stringify(networkMapPosition));
      }
    });
  }

  init(
    networkId: number,
    page: NetworkMapPage,
    mapPositionFromUrl: CachedMapPosition,
    urlLayerIds: string[]
  ): void {
    this.networkId = networkId;
    this.registerLayers(page, urlLayerIds);

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
        const mapPosition: CachedMapPosition = JSON.parse(mapPositionString);
        if (networkId.toString() === mapPosition.id) {
          this.gotoLastKnownPosition(mapPosition);
        } else {
          view.fit(Util.toExtent(page.bounds, 0.1));
        }
      }
    }

    this.mapZoomService.install(view);
    this.mapClickService.installOn(this.map);

    this.finalizeSetup(true);
  }

  private registerLayers(page: NetworkMapPage, urlLayerIds: string[]): void {
    const registry = new MapLayerRegistry();
    registry.register(urlLayerIds, BackgroundLayer.build(), true);
    registry.register(urlLayerIds, OsmLayer.build(), false);
    const networkNodesLayers = [
      NetworkNodesBitmapTileLayer.build(page.summary.networkType),
      NetworkNodesVectorTileLayer.build(
        page.summary.networkType,
        page.networkNodeIds,
        page.connectionNodeIds,
        page.networkRouteIds,
        page.connectionRouteIds
      ),
    ];
    registry.registerAll(urlLayerIds, networkNodesLayers, true);
    registry.register(urlLayerIds, NetworkNodesMarkerLayer.build(page.nodes), true);
    OpenDataLayers.register(registry, page.summary.networkType, urlLayerIds);
    registry.register(urlLayerIds, TileDebug256Layer.build(), false);
    this.register(registry);
  }

  private gotoLastKnownPosition(mapPosition: CachedMapPosition): void {
    this.map.getView().setZoom(mapPosition.zoom);
    this.map.getView().setRotation(mapPosition.rotation);
    const center: Coordinate = [mapPosition.x, mapPosition.y];
    this.map.getView().setCenter(center);
  }
}
