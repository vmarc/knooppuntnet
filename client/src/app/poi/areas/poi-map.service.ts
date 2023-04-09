import { Injectable } from '@angular/core';
import { OpenlayersMapService } from '@app/components/ol/services/openlayers-map-service';
import { MapLayerRegistry } from '@app/components/ol/layers/map-layer-registry';
import { BackgroundLayer } from '@app/components/ol/layers/background-layer';
import { OsmLayer } from '@app/components/ol/layers/osm-layer';
import { MapLayerService } from '@app/components/ol/services/map-layer.service';
import { NetworkType } from '@api/custom/network-type';
import { PoiAreasLayer } from '@app/components/ol/layers/poi-areas-layer';
import Map from 'ol/Map';
import { MapControls } from '@app/components/ol/layers/map-controls';
import View from 'ol/View';
import { ZoomLevel } from '@app/components/ol/domain/zoom-level';
import { Util } from '@app/components/shared/util';

@Injectable()
export class PoiMapService extends OpenlayersMapService {
  constructor(private mapLayerService: MapLayerService) {
    super();
  }

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
      this.mapLayerService.networkBitmapTileLayer(
        NetworkType.cycling,
        'analysis'
      ),
      true
    );

    registry.register([], PoiAreasLayer.build(geoJson), true);

    this.register(registry);
  }
}
