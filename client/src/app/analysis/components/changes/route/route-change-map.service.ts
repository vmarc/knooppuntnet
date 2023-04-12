import { Injectable } from '@angular/core';
import { Bounds } from '@api/common';
import { RawNode } from '@api/common/data/raw';
import { GeometryDiff } from '@api/common/route';
import { ZoomLevel } from '@app/components/ol/domain';
import { BackgroundLayer } from '@app/components/ol/layers';
import { MapControls } from '@app/components/ol/layers';
import { MapLayerRegistry } from '@app/components/ol/layers';
import { OsmLayer } from '@app/components/ol/layers';
import { RouteNodesLayer } from '@app/components/ol/layers';
import { RouteChangeLayers } from '@app/components/ol/layers';
import { OpenlayersMapService } from '@app/components/ol/services';
import { Util } from '@app/components/shared';
import { I18nService } from '@app/i18n';
import Map from 'ol/Map';
import View from 'ol/View';

@Injectable()
export class RouteChangeMapService extends OpenlayersMapService {
  constructor(private i18nService: I18nService) {
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
    new RouteChangeLayers(this.i18nService)
      .build(geometryDiff)
      .forEach((mapLayer) => registry.register([], mapLayer, true));
    this.register(registry);
  }
}
