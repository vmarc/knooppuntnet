import { Injectable } from '@angular/core';
import { OlUtil } from '@app/ol/ol-util';
import { ZoomLevel } from '@app/ol/domain/zoom-level';
import { NetworkBitmapTileLayer } from '@app/ol/layers/network-bitmap-tile-layer';
import { OldMapLayerRegistry } from '@app/ol/layers/old-map-layer-registry';
import { OldOsmLayer } from '@app/ol/layers/old-osm-layer';
import { PoiAreasLayer } from '@app/ol/layers/poi-areas-layer';
import { OldBackgroundLayer } from '@app/ol/layers/old-background-layer';
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
    const registry = new OldMapLayerRegistry();
    registry.register([], OldBackgroundLayer.build(), true);
    registry.register([], OldOsmLayer.build(), false);

    registry.register([], NetworkBitmapTileLayer.build('cycling', 'analysis'), true);

    registry.register([], PoiAreasLayer.build(geoJson), true);

    this.register(registry);
  }
}
