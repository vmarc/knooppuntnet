import { inject } from '@angular/core';
import { Injectable } from '@angular/core';
import { OldPoiService } from '@app/shared/services/old-poi.service';
import { StyleFunction } from 'ol/style/Style';
import { ZoomLevel } from '../domain/zoom-level';
import { OldPoiTileLayer } from '../layers/old-poi-tile-layer';
import { OldMapLayer } from '../layers/old-map-layer';
import { OldPoiStyleMap } from '../style/old-poi-style-map';

@Injectable({
  providedIn: 'root',
})
export class OldPoiTileLayerService {
  private readonly poiService = inject(OldPoiService);

  static poiLayerId = 'pois';
  poiStyleMap: OldPoiStyleMap;

  constructor() {
    console.log('OldPoiTileLayerService.constructor');
    this.poiService.poiConfiguration.subscribe((configuration) => {
      if (configuration !== null) {
        this.poiStyleMap = new OldPoiStyleMap(configuration);
      }
    });
  }

  public buildLayer(): OldMapLayer {
    const layer = new OldPoiTileLayer().build();
    layer.setStyle(this.poiStyleFunction());
    this.poiService.changeCount.subscribe(() => layer.changed());
    return new OldMapLayer(
      OldPoiTileLayerService.poiLayerId,
      OldPoiTileLayerService.poiLayerId,
      ZoomLevel.poiTileMinZoom,
      ZoomLevel.vectorTileMaxOverZoom,
      'vector',
      layer,
      null,
      null
    );
  }

  private poiStyleFunction(): StyleFunction {
    return (feature, resolution) => {
      if (this.poiStyleMap) {
        const layer = feature.get('layer');
        if (layer != null) {
          if (this.poiService.isPoiActive(layer)) {
            const style = this.poiStyleMap.get(layer);
            if (style != null) {
              return [style];
            }
          }
        }
      }
      return null;
    };
  }
}
