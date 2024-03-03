import { inject } from '@angular/core';
import { Injectable } from '@angular/core';
import { PoiService } from '@app/services';
import { StyleFunction } from 'ol/style/Style';
import { ZoomLevel } from '../domain';
import { PoiTileLayer } from '../layers';
import { MapLayer } from '../layers';
import { PoiStyleMap } from '../style';

@Injectable({
  providedIn: 'root',
})
export class PoiTileLayerService {
  private readonly poiService = inject(PoiService);

  static poiLayerId = 'pois';
  poiStyleMap: PoiStyleMap;

  constructor() {
    console.log('PoiTileLayerService.constructor');
    this.poiService.poiConfiguration.subscribe((configuration) => {
      if (configuration !== null) {
        this.poiStyleMap = new PoiStyleMap(configuration);
      }
    });
  }

  public buildLayer(): MapLayer {
    const layer = new PoiTileLayer().build();
    layer.setStyle(this.poiStyleFunction());
    this.poiService.changeCount.subscribe(() => layer.changed());
    return new MapLayer(
      PoiTileLayerService.poiLayerId,
      PoiTileLayerService.poiLayerId,
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
