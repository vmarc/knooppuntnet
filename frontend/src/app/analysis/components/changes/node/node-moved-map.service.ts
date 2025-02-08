import { Injectable } from '@angular/core';
import { NodeMoved } from '@api/common/diff/node/node-moved';
import { OlUtil } from '@app/ol/ol-util';
import { ZoomLevel } from '@app/ol/domain/zoom-level';
import { OldBackgroundLayer } from '@app/ol/layers/old-background-layer';
import { OldOsmLayer } from '@app/ol/layers/old-osm-layer';
import { MapControls } from '@app/ol/layers/map-controls';
import { NodeMovedLayer } from '@app/ol/layers/node-moved-layer';
import { OldMapLayerRegistry } from '@app/ol/layers/old-map-layer-registry';
import { OpenlayersMapService } from '@app/ol/services/openlayers-map-service';
import Map from 'ol/Map';
import View from 'ol/View';

@Injectable()
export class NodeMovedMapService extends OpenlayersMapService {
  init(nodeMoved: NodeMoved): void {
    this.registerLayers(nodeMoved);
    this.initMap(
      new Map({
        target: this.mapId,
        layers: this.layers,
        controls: MapControls.build(),
        view: new View({
          minZoom: ZoomLevel.minZoom,
          maxZoom: ZoomLevel.maxZoom,
          zoom: 18,
        }),
      })
    );
    const center = OlUtil.latLonToCoordinate(nodeMoved.after);
    this.map.getView().setCenter(center);
    this.finalizeSetup();
  }

  private registerLayers(nodeMoved: NodeMoved): void {
    const registry = new OldMapLayerRegistry();
    registry.register([], OldBackgroundLayer.build(), true);
    registry.register([], OldOsmLayer.build(), false);
    registry.register([], NodeMovedLayer.build(nodeMoved), true);
    this.register(registry);
  }
}
