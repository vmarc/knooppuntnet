import { blue } from '@app/ol/style/style-color';
import { MVT } from 'ol/format';
import VectorTileLayer from 'ol/layer/VectorTile';
import VectorTile from 'ol/source/VectorTile';
import Stroke from 'ol/style/Stroke';
import Style from 'ol/style/Style';
import { StyleFunction } from 'ol/style/Style';
import { OldMapLayer } from './old-map-layer';
import { OldLayers } from './old-layers';

export class MonitorLayer {
  static build(): OldMapLayer {
    const source = new VectorTile({
      minZoom: 2,
      maxZoom: 14,
      format: new MVT(),
      url: '/tiles/monitor/{z}/{x}/{y}.mvt',
    });

    const layer = new VectorTileLayer({
      zIndex: OldLayers.zIndexNetworkLayer,
      className: `monitor`,
      declutter: false,
      source,
      renderMode: 'vector',
      style: this.styleFunction(),
    });

    const name = $localize`:@@map.layer.monitor:All monitor routes`;
    return new OldMapLayer('monitor', name, 2, 22, 'vector', layer, null, null);
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
