import { Injectable } from '@angular/core';
import { NodeMapInfo } from '@api/common';
import { NetworkType } from '@api/custom';
import { OlUtil } from '@app/ol';
import { MapPosition } from '@app/ol/domain';
import { ZoomLevel } from '@app/ol/domain';
import { BackgroundLayer } from '@app/ol/layers';
import { MapControls } from '@app/ol/layers';
import { MapLayerRegistry } from '@app/ol/layers';
import { NetworkVectorTileLayer } from '@app/ol/layers';
import { NodeMarkerLayer } from '@app/ol/layers';
import { OsmLayer } from '@app/ol/layers';
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
  constructor(private mapClickService: MapClickService) {
    super();
  }

  init(
    nodeMapInfo: NodeMapInfo,
    defaultNetworkType: NetworkType,
    mapPositionFromUrl: MapPosition
  ): void {
    this.registerLayers(nodeMapInfo, defaultNetworkType);

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
      const center = OlUtil.toCoordinate(
        nodeMapInfo.latitude,
        nodeMapInfo.longitude
      );
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
    defaultNetworkType: NetworkType
  ): void {
    const registry = new MapLayerRegistry();
    registry.register([], BackgroundLayer.build(), true);
    registry.register([], OsmLayer.build(), false);

    nodeMapInfo.networkTypes.forEach((networkType) => {
      const visible =
        nodeMapInfo.networkTypes.length > 1
          ? networkType == defaultNetworkType
          : true;
      registry.register(
        [],
        NetworkVectorTileLayer.build(
          networkType,
          new NodeMapStyle().styleFunction()
        ),
        visible
      );
    });

    registry.register([], NodeMarkerLayer.build(nodeMapInfo), true);
    registry.register([], TileDebug256Layer.build(), false);

    this.register(registry);
  }
}
