import { inject } from '@angular/core';
import { Injectable } from '@angular/core';
import { OlUtil } from '@app/ol/ol-util';
import { OldOldPoiService } from '@app/shared/services/old-old-poi.service';
import { StyleFunction } from 'ol/style/Style';
import { ZoomLevel } from '../domain/zoom-level';
import { OldOldPoiTileLayer } from '../layers/old-old-poi-tile-layer';
import { OldOldMapLayer } from '../layers/old-old-map-layer';
import { OldOldPoiStyleMap } from '../style/old-old-poi-style-map';

@Injectable({
  providedIn: 'root',
})
export class OldPoiTileLayerService {
  private readonly poiService = inject(OldOldPoiService);

  static poiLayerId = 'pois';
  poiStyleMap: OldOldPoiStyleMap;

  constructor() {
    console.log('OldPoiTileLayerService.constructor');
    this.poiService.poiConfiguration.subscribe((configuration) => {
      if (configuration !== null) {
        this.poiStyleMap = new OldOldPoiStyleMap(configuration);
      }
    });
  }

  public buildLayer(): OldOldMapLayer {
    const layer = new OldOldPoiTileLayer().build();
    layer.setStyle(this.poiStyleFunction());
    this.poiService.changeCount.subscribe(() => layer.changed());
    return new OldOldMapLayer(
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
        const featureLayer = OlUtil.featureLayer(feature);
        if (featureLayer != null) {
          if (this.poiService.isPoiActive(featureLayer)) {
            const style = this.poiStyleMap.get(featureLayer);
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
