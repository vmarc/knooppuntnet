import { Injectable } from '@angular/core';
import { OlUtil } from '@app/ol/ol-util';
import { ZoomLevel } from '@app/ol/domain/zoom-level';
import { NetworkBitmapTileLayer } from '@app/ol/layers/network-bitmap-tile-layer';
import { OldOldMapLayerRegistry } from '@app/ol/layers/old-old-map-layer-registry';
import { PoiAreasLayer } from '@app/ol/layers/poi-areas-layer';
import { MapControls } from '@app/ol/layers/map-controls';
import { OpenlayersMapService } from '@app/ol/services/openlayers-map-service';
import Map from 'ol/Map';
import View from 'ol/View';

@Injectable()
export class PoiMapService extends OpenlayersMapService {
  init(geoJson: string): void {
    this.registerLayers(geoJson);
    const center = OlUtil.toCoordinate('49.153', '2.4609');
    this.initMap(
      new Map({
        target: this.mapId,
        layers: this.layers,
        controls: MapControls.build(),
        view: new View({
          center,
          minZoom: ZoomLevel.minZoom,
          maxZoom: ZoomLevel.maxZoom,
          zoom: 6,
        }),
      })
    );

    this.finalizeSetup();
  }

  private registerLayers(geoJson: string): void {
    const registry = new OldOldMapLayerRegistry();

    registry.register([], NetworkBitmapTileLayer.build('cycling', 'analysis'), true);

    registry.register([], PoiAreasLayer.build(geoJson), true);

    this.register(registry);
  }
}
