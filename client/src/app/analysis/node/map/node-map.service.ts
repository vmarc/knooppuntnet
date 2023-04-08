import { Injectable } from '@angular/core';
import { BackgroundLayer } from '@app/components/ol/layers/background-layer';
import { NodeMarkerLayer } from '@app/components/ol/layers/node-marker-layer';
import { TileDebug256Layer } from '@app/components/ol/layers/tile-debug-256-layer';
import { NetworkVectorTileLayer } from '@app/components/ol/layers/network-vector-tile-layer';
import { NodeMapStyle } from '@app/components/ol/style/node-map-style';
import { NodeMapInfo } from '@api/common/node-map-info';
import { OsmLayer } from '@app/components/ol/layers/osm-layer';
import { NetworkType } from '@api/custom/network-type';
import { OpenlayersMapService } from '@app/components/ol/services/openlayers-map-service';
import { MapLayerRegistry } from '@app/components/ol/layers/map-layer-registry';
import { ViewOptions } from 'ol/View';
import View from 'ol/View';
import { ZoomLevel } from '@app/components/ol/domain/zoom-level';
import { Coordinate } from 'ol/coordinate';
import { Util } from '@app/components/shared/util';
import { MapControls } from '@app/components/ol/layers/map-controls';
import Map from 'ol/Map';
import { MapClickService } from '@app/components/ol/services/map-click.service';
import { OldMapPositionService } from '@app/components/ol/services/old-map-position.service';
import { MapPosition } from '@app/components/ol/domain/map-position';

@Injectable()
export class NodeMapService extends OpenlayersMapService {
  constructor(
    private mapClickService: MapClickService,
    private mapPositionService: OldMapPositionService
  ) {
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
    this.mapPositionService.install(this.map.getView());

    this.finalizeSetup();
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
