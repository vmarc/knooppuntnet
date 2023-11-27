import { Injectable } from '@angular/core';
import { PoiDetail } from '@api/common';
import { OlUtil } from '@app/ol';
import Map from 'ol/Map';
import View from 'ol/View';
import { ViewOptions } from 'ol/View';
import { ZoomLevel } from '../domain';
import { BackgroundLayer } from '../layers';
import { MapControls } from '../layers';
import { MapLayerRegistry } from '../layers';
import { OsmLayer } from '../layers';
import { PoiMarkerLayer } from '../layers';
import { OpenlayersMapService } from '../services';

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
    const registry = new MapLayerRegistry();
    registry.register([], BackgroundLayer.build(), true);
    registry.register([], OsmLayer.build(), false);
    registry.register([], PoiMarkerLayer.build(poiDetail), true);
    this.register(registry);
  }
}
