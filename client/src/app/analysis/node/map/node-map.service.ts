import { Injectable } from '@angular/core';
import { NodeMapInfo } from '@api/common/node-map-info';
import { NetworkType } from '@api/custom/network-type';
import { MapPosition } from '@app/components/ol/domain/map-position';
import { ZoomLevel } from '@app/components/ol/domain/zoom-level';
import { BackgroundLayer } from '@app/components/ol/layers/background-layer';
import { MapControls } from '@app/components/ol/layers/map-controls';
import { MapLayerRegistry } from '@app/components/ol/layers/map-layer-registry';
import { NetworkVectorTileLayer } from '@app/components/ol/layers/network-vector-tile-layer';
import { NodeMarkerLayer } from '@app/components/ol/layers/node-marker-layer';
import { OsmLayer } from '@app/components/ol/layers/osm-layer';
import { TileDebug256Layer } from '@app/components/ol/layers/tile-debug-256-layer';
import { MapClickService } from '@app/components/ol/services/map-click.service';
import { OpenlayersMapService } from '@app/components/ol/services/openlayers-map-service';
import { NodeMapStyle } from '@app/components/ol/style/node-map-style';
import { Util } from '@app/components/shared/util';
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
      const center = Util.toCoordinate(
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
