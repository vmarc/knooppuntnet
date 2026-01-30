import { Injectable } from '@angular/core';
import { PoiDetail } from '@api/common/poi-detail';
import { OlUtil } from '@app/ol/ol-util';
import Map from 'ol/Map';
import View from 'ol/View';
import { ViewOptions } from 'ol/View';
import { ZoomLevel } from '../domain/zoom-level';
import { MapControls } from '../layers/map-controls';
import { OldOldMapLayerRegistry } from '../layers/old-old-map-layer-registry';
import { PoiMarkerLayer } from '../layers/poi-marker-layer';
import { OpenlayersMapService } from '../services/openlayers-map-service';

@Injectable()
export class PoiDetailMapService extends OpenlayersMapService {
  init(poiDetail: PoiDetail): void {
    this.registerLayers(poiDetail);

    let viewOptions: ViewOptions = {
      minZoom: ZoomLevel.vectorTileMinZoom,
      maxZoom: ZoomLevel.maxZoom,
    };

    const center = OlUtil.toCoordinate(poiDetail.poi.latitude, poiDetail.poi.longitude);
    viewOptions = {
      ...viewOptions,
      center,
      zoom: 18,
    };

    this.initMap(
      new Map({
        target: this.mapId,
        layers: this.layers,
        controls: MapControls.build(),
        view: new View(viewOptions),
      })
    );

    this.finalizeSetup();
  }

  private registerLayers(poiDetail: PoiDetail): void {
    const registry = new OldOldMapLayerRegistry();
    registry.register([], PoiMarkerLayer.build(poiDetail), true);
    this.register(registry);
  }
}
