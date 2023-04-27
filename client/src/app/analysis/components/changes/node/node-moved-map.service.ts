import { Injectable } from '@angular/core';
import { NodeMoved } from '@api/common/diff/node';
import { OlUtil } from '@app/components/ol';
import { ZoomLevel } from '@app/components/ol/domain';
import { BackgroundLayer } from '@app/components/ol/layers';
import { OsmLayer } from '@app/components/ol/layers';
import { MapControls } from '@app/components/ol/layers';
import { NodeMovedLayer } from '@app/components/ol/layers';
import { MapLayerRegistry } from '@app/components/ol/layers';
import { OpenlayersMapService } from '@app/components/ol/services';
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
    const registry = new MapLayerRegistry();
    registry.register([], BackgroundLayer.build(), true);
    registry.register([], OsmLayer.build(), false);
    registry.register([], NodeMovedLayer.build(nodeMoved), true);
    this.register(registry);
  }
}
