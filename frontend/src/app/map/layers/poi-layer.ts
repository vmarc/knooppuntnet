import { Signal } from '@angular/core';
import { ZoomLevel } from '@app/ol/domain';
import { MVT } from 'ol/format';
import VectorTileLayer from 'ol/layer/VectorTile';
import VectorTile from 'ol/source/VectorTile';
import { StyleFunction } from 'ol/style/Style';
import { PoiStyleMap } from '../style/poi-style-map';
import { Layers } from './layers';

export class PoiLayer {
  static build(
    poiStyleMap: Signal<PoiStyleMap>,
    poiActive: Signal<ReadonlyMap<string, boolean>>
  ): VectorTileLayer {
    const source = new VectorTile({
      tileSize: 256,
      minZoom: ZoomLevel.poiTileMinZoom,
      maxZoom: ZoomLevel.poiTileMaxZoom,
      format: new MVT(),
      url: '/tiles/poi/{z}/{x}/{y}.mvt',
    });

    return new VectorTileLayer({
      zIndex: Layers.zIndexPoiLayer,
      source,
      renderBuffer: 40,
      declutter: false,
      className: 'poi',
      renderMode: 'vector',
      style: this.styleFunction(poiStyleMap, poiActive),
    });
  }

  private static styleFunction(
    poiStyleMap: Signal<PoiStyleMap>,
    poiActive: Signal<ReadonlyMap<string, boolean>>
  ): StyleFunction {
    return (feature, resolution) => {
      // console.log('styleFunction()', poiStyleMap(), poiActive(), feature);
      if (poiStyleMap()) {
        const layer = feature.get('layer');
        if (layer != null && poiActive()) {
          if (poiActive().get(layer) === true) {
            const style = poiStyleMap().get(layer);
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
