import { Injectable } from '@angular/core';
import { Bounds } from '@api/common/bounds';
import { GeometryDiff } from '@api/common/route/geometry-diff';
import { RouteNodeChange } from '@api/common/route/route-node-change';
import { ZoomLevel } from '@app/ol/domain/zoom-level';
import { MapControls } from '@app/ol/layers/map-controls';
import { OldOldMapLayerRegistry } from '@app/ol/layers/old-old-map-layer-registry';
import { RouteNodesLayer } from '@app/ol/layers/route-nodes-layer';
import { RouteChangeLayers } from '@app/ol/layers/route-change-layers';
import { OpenlayersMapService } from '@app/ol/services/openlayers-map-service';
import { Util } from '@app/shared/components/util';
import Map from 'ol/Map';
import View from 'ol/View';

@Injectable()
export class RouteChangeMapService extends OpenlayersMapService {
  init(
    geometryDiff: GeometryDiff,
    nodeChanges: ReadonlyArray<RouteNodeChange>,
    bounds: Bounds
  ): void {
    this.registerLayers(geometryDiff, nodeChanges);

    this.initMap(
      new Map({
        target: this.mapId,
        layers: this.layers,
        controls: MapControls.build(),
        view: new View({
          minZoom: ZoomLevel.newMinZoom,
          maxZoom: ZoomLevel.maxZoom,
        }),
      })
    );
    this.map.getView().fit(Util.toExtent(bounds, 0.1));

    this.finalizeSetup();
  }

  private registerLayers(
    geometryDiff: GeometryDiff,
    nodeChanges: ReadonlyArray<RouteNodeChange>
  ): void {
    const registry = new OldOldMapLayerRegistry();
    if (nodeChanges && nodeChanges.length > 0) {
      registry.register([], RouteNodesLayer.build(nodeChanges), true);
    }
    new RouteChangeLayers()
      .build(geometryDiff)
      .forEach((mapLayer) => registry.register([], mapLayer, true));
    this.register(registry);
  }
}
