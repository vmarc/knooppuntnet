import { Signal } from '@angular/core';
import { ZoomLevel } from '@app/ol/domain/zoom-level';
import { MVT } from 'ol/format';
import VectorTileLayer from 'ol/layer/VectorTile';
import VectorTile from 'ol/source/VectorTile';
import { StyleFunction } from 'ol/style/Style';
import { PoiStyleMap } from '../../state/poi/poi-style-map';
import { Layers } from './layers';
import { MapLayer } from './map-layer';

export class PoiLayer {
  static build(
    poiStyleMap: Signal<PoiStyleMap>,
    poiActive: Signal<ReadonlyMap<string, boolean>>
  ): MapLayer {
    const source = new VectorTile({
      tileSize: 256,
      minZoom: ZoomLevel.poiTileMinZoom,
      maxZoom: ZoomLevel.poiTileMaxZoom,
      format: new MVT(),
      url: '/tiles/poi/{z}/{x}/{y}.mvt',
    });

    const layer = new VectorTileLayer({
      zIndex: Layers.zIndexPoiLayer,
      source,
      renderBuffer: 40,
      declutter: false,
      className: 'poi',
      renderMode: 'vector',
      style: this.styleFunction(poiStyleMap, poiActive),
    });

    return {
      layerType: 'poi',
      minZoom: 11,
      maxZoom: 15,
      layer: layer,
    };
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
