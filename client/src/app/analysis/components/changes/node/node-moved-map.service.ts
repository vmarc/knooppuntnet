import { Injectable } from '@angular/core';
import { NewMapService } from '@app/components/ol/services/new-map.service';
import { BackgroundLayer } from '@app/components/ol/layers/background-layer';
import { OsmLayer } from '@app/components/ol/layers/osm-layer';
import { MapControls } from '@app/components/ol/layers/map-controls';
import View from 'ol/View';
import { ZoomLevel } from '@app/components/ol/domain/zoom-level';
import { Util } from '@app/components/shared/util';
import { NodeMovedLayer } from '@app/components/ol/layers/node-moved-layer';
import { NodeMoved } from '@api/common/diff/node/node-moved';
import { MapLayerRegistry } from '@app/components/ol/layers/map-layer-registry';
import { OpenlayersMapService } from '@app/components/ol/services/openlayers-map-service';

@Injectable()
export class NodeMovedMapService extends OpenlayersMapService {
  constructor(newMapService: NewMapService) {
    super(newMapService);
  }

  init(nodeMoved: NodeMoved): void {
    const mapLayers = new MapLayerRegistry();
    mapLayers.register(BackgroundLayer.build(), true);
    mapLayers.register(OsmLayer.build(), false);
    mapLayers.register(NodeMovedLayer.build(nodeMoved), true);

    this.register(mapLayers);

    this._map = this.newMapService.build({
      target: this.mapId,
      layers: this.layers,
      controls: MapControls.build(),
      view: new View({
        minZoom: ZoomLevel.minZoom,
        maxZoom: ZoomLevel.maxZoom,
        zoom: 18,
      }),
    });
    const center = Util.latLonToCoordinate(nodeMoved.after);
    this._map.map.getView().setCenter(center);
  }
}
