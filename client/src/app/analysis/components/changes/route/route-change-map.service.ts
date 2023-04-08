import { Injectable } from '@angular/core';
import { MapControls } from '@app/components/ol/layers/map-controls';
import View from 'ol/View';
import { ZoomLevel } from '@app/components/ol/domain/zoom-level';
import { Util } from '@app/components/shared/util';
import { BackgroundLayer } from '@app/components/ol/layers/background-layer';
import { OsmLayer } from '@app/components/ol/layers/osm-layer';
import { RouteNodesLayer } from '@app/components/ol/layers/route-nodes-layer';
import { MapLayerService } from '@app/components/ol/services/map-layer.service';
import { GeometryDiff } from '@api/common/route/geometry-diff';
import { RawNode } from '@api/common/data/raw/raw-node';
import { Bounds } from '@api/common/bounds';
import { MapLayerRegistry } from '@app/components/ol/layers/map-layer-registry';
import { OpenlayersMapService } from '@app/components/ol/services/openlayers-map-service';
import Map from 'ol/Map';

@Injectable()
export class RouteChangeMapService extends OpenlayersMapService {
  constructor(private mapLayerService: MapLayerService) {
    super();
  }

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
    registry.register([], RouteNodesLayer.build(nodes), true);
    this.mapLayerService
      .routeChangeLayers(geometryDiff)
      .forEach((mapLayer) => registry.register([], mapLayer, true));
    this.register(registry);
  }
}
