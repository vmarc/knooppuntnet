import { blue } from '@app/ol/style';
import { MVT } from 'ol/format';
import VectorTileLayer from 'ol/layer/VectorTile';
import VectorTile from 'ol/source/VectorTile';
import Stroke from 'ol/style/Stroke';
import Style from 'ol/style/Style';
import { StyleFunction } from 'ol/style/Style';
import { Layers } from './layers';
import { MapLayer } from './map-layer';

export class MonitorLayer {
  static id = 'monitor';

  static build(): MapLayer {
    const source = new VectorTile({
      tileSize: 512,
      minZoom: 2,
      maxZoom: 14,
      format: new MVT(),
      url: '/tiles-history/monitor/{z}/{x}/{y}.mvt',
    });

    const layer = new VectorTileLayer({
      zIndex: Layers.zIndexNetworkLayer,
      className: `monitor`,
      declutter: false,
      source,
      renderMode: 'vector',
      style: this.styleFunction(),
    });

    return new MapLayer(this.id, this.id, 2, 22, layer, null, null);
  }

  static styleFunction(): StyleFunction {
    return (feature, resolution) => {
      return new Style({
        stroke: new Stroke({
          color: blue,
          width: 2,
        }),
      });
    };
  }
}
