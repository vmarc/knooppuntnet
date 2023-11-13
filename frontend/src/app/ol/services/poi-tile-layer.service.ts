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
  static poiLayerName = 'pois';
  poiStyleMap: PoiStyleMap;

  constructor(private poiService: PoiService) {
    poiService.poiConfiguration.subscribe((configuration) => {
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
      PoiTileLayerService.poiLayerName,
      PoiTileLayerService.poiLayerName,
      ZoomLevel.poiTileMinZoom,
      ZoomLevel.vectorTileMaxOverZoom,
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
