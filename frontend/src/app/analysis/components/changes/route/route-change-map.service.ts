import { Injectable } from '@angular/core';
import { Bounds } from '@api/common';
import { RawNode } from '@api/common/data/raw';
import { GeometryDiff } from '@api/common/route';
import { Util } from '@app/components/shared';
import { ZoomLevel } from '@app/ol/domain';
import { BackgroundLayer } from '@app/ol/layers';
import { MapControls } from '@app/ol/layers';
import { MapLayerRegistry } from '@app/ol/layers';
import { OsmLayer } from '@app/ol/layers';
import { RouteNodesLayer } from '@app/ol/layers';
import { RouteChangeLayers } from '@app/ol/layers';
import { OpenlayersMapService } from '@app/ol/services';
import Map from 'ol/Map';
import View from 'ol/View';

@Injectable()
export class RouteChangeMapService extends OpenlayersMapService {
  init(geometryDiff: GeometryDiff, nodes: RawNode[], bounds: Bounds): void {
    this.registerLayers(geometryDiff, nodes);

    this.initMap(
      new Map({
        target: this.mapId,
        layers: this.layers,
        controls: MapControls.build(),
        view: new View({
          minZoom: ZoomLevel.vectorTileMinZoom,
          maxZoom: ZoomLevel.maxZoom,
        }),
      })
    );
    this.map.getView().fit(Util.toExtent(bounds, 0.1));

    this.finalizeSetup();
  }

  private registerLayers(geometryDiff: GeometryDiff, nodes: RawNode[]): void {
    const registry = new MapLayerRegistry();
    registry.register([], BackgroundLayer.build(), true);
    registry.register([], OsmLayer.build(), false);
    if (nodes && nodes.length > 0) {
      registry.register([], RouteNodesLayer.build(nodes), true);
    }
    new RouteChangeLayers()
      .build(geometryDiff)
      .forEach((mapLayer) => registry.register([], mapLayer, true));
    this.register(registry);
  }
}
