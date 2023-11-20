import { Injectable } from '@angular/core';
import { NetworkType } from '@api/custom';
import { OlUtil } from '@app/ol';
import { ZoomLevel } from '@app/ol/domain';
import { NetworkBitmapTileLayer } from '@app/ol/layers';
import { MapLayerRegistry } from '@app/ol/layers';
import { OsmLayer } from '@app/ol/layers';
import { PoiAreasLayer } from '@app/ol/layers';
import { BackgroundLayer } from '@app/ol/layers';
import { MapControls } from '@app/ol/layers';
import { OpenlayersMapService } from '@app/ol/services';
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
    const registry = new MapLayerRegistry();
    registry.register([], BackgroundLayer.build(), true);
    registry.register([], OsmLayer.build(), false);

    registry.register(
      [],
      NetworkBitmapTileLayer.build(NetworkType.cycling, 'analysis'),
      true
    );

    registry.register([], PoiAreasLayer.build(geoJson), true);

    this.register(registry);
  }
}
