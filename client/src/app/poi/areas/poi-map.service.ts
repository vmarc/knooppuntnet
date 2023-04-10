import { Injectable } from '@angular/core';
import { NetworkType } from '@api/custom/network-type';
import { ZoomLevel } from '@app/components/ol/domain/zoom-level';
import { BackgroundLayer } from '@app/components/ol/layers/background-layer';
import { MapControls } from '@app/components/ol/layers/map-controls';
import { MapLayerRegistry } from '@app/components/ol/layers/map-layer-registry';
import { OsmLayer } from '@app/components/ol/layers/osm-layer';
import { PoiAreasLayer } from '@app/components/ol/layers/poi-areas-layer';
import { OpenlayersMapService } from '@app/components/ol/services/openlayers-map-service';
import { Util } from '@app/components/shared/util';
import Map from 'ol/Map';
import View from 'ol/View';
import { NetworkBitmapTileLayer } from '../../components/ol/layers/network-bitmap-tile-layer';

@Injectable()
export class PoiMapService extends OpenlayersMapService {
  init(geoJson: string): void {
    this.registerLayers(geoJson);
    const center = Util.toCoordinate('49.153', '2.4609');
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
