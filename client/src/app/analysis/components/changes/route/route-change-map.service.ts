import { Injectable } from '@angular/core';
import { Bounds } from '@api/common/bounds';
import { RawNode } from '@api/common/data/raw/raw-node';
import { GeometryDiff } from '@api/common/route/geometry-diff';
import { ZoomLevel } from '@app/components/ol/domain/zoom-level';
import { BackgroundLayer } from '@app/components/ol/layers/background-layer';
import { MapControls } from '@app/components/ol/layers/map-controls';
import { MapLayerRegistry } from '@app/components/ol/layers/map-layer-registry';
import { OsmLayer } from '@app/components/ol/layers/osm-layer';
import { RouteNodesLayer } from '@app/components/ol/layers/route-nodes-layer';
import { OpenlayersMapService } from '@app/components/ol/services/openlayers-map-service';
import { Util } from '@app/components/shared/util';
import Map from 'ol/Map';
import View from 'ol/View';
import { RouteChangeLayers } from '../../../../components/ol/layers/route-change-layers';
import { I18nService } from '../../../../i18n/i18n.service';

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
