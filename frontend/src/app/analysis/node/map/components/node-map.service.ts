import { inject } from '@angular/core';
import { Injectable } from '@angular/core';
import { NodeMapInfo } from '@api/common';
import { NetworkType } from '@api/custom';
import { OlUtil } from '@app/ol';
import { MapPosition } from '@app/ol/domain';
import { ZoomLevel } from '@app/ol/domain';
import { OpenDataLayers } from '@app/ol/layers';
import { OldBackgroundLayer } from '@app/ol/layers';
import { MapControls } from '@app/ol/layers';
import { MapLayerRegistry } from '@app/ol/layers';
import { NetworkVectorTileLayer } from '@app/ol/layers';
import { NodeMarkerLayer } from '@app/ol/layers';
import { OldOsmLayer } from '@app/ol/layers';
import { TileDebug256Layer } from '@app/ol/layers';
import { MapClickService } from '@app/ol/services';
import { OpenlayersMapService } from '@app/ol/services';
import { NodeMapStyle } from '@app/ol/style';
import { Coordinate } from 'ol/coordinate';
import Map from 'ol/Map';
import { ViewOptions } from 'ol/View';
import View from 'ol/View';

@Injectable()
export class NodeMapService extends OpenlayersMapService {
  private readonly mapClickService = inject(MapClickService);

  init(
    nodeMapInfo: NodeMapInfo,
    defaultNetworkType: NetworkType,
    mapPositionFromUrl: MapPosition,
    urlLayerIds: string[]
  ): void {
    this.registerLayers(nodeMapInfo, defaultNetworkType, urlLayerIds);

    let viewOptions: ViewOptions = {
      minZoom: ZoomLevel.vectorTileMinZoom,
      maxZoom: ZoomLevel.maxZoom,
    };

    if (mapPositionFromUrl) {
      const center: Coordinate = [mapPositionFromUrl.x, mapPositionFromUrl.y];
      const zoom = mapPositionFromUrl.zoom;
      viewOptions = {
        ...viewOptions,
        center,
        zoom,
      };
    } else {
      const center = OlUtil.toCoordinate(nodeMapInfo.latitude, nodeMapInfo.longitude);
      viewOptions = {
        ...viewOptions,
        center,
        zoom: 18,
      };
    }

    this.initMap(
      new Map({
        target: this.mapId,
        layers: this.layers,
        controls: MapControls.build(),
        view: new View(viewOptions),
      })
    );

    this.mapClickService.installOn(this.map);

    this.finalizeSetup(true);
  }

  private registerLayers(
    nodeMapInfo: NodeMapInfo,
    defaultNetworkType: NetworkType,
    urlLayerIds: string[]
  ): void {
    const registry = new MapLayerRegistry();
    registry.register(urlLayerIds, OldBackgroundLayer.build(), true);
    registry.register(urlLayerIds, OldOsmLayer.build(), false);

    nodeMapInfo.networkTypes.forEach((networkType) => {
      const visible =
        nodeMapInfo.networkTypes.length > 1 ? networkType == defaultNetworkType : true;
      registry.register(
        urlLayerIds,
        NetworkVectorTileLayer.build(networkType, new NodeMapStyle().styleFunction()),
        visible
      );
    });

    registry.register(urlLayerIds, NodeMarkerLayer.build(nodeMapInfo), true);

    nodeMapInfo.networkTypes.forEach((networkType) =>
      OpenDataLayers.register(registry, networkType, urlLayerIds)
    );

    registry.register(urlLayerIds, TileDebug256Layer.build(), false);

    this.register(registry);
  }
}
