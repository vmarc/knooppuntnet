import VectorLayer from 'ol/layer/Vector';
import VectorSource from 'ol/source/Vector';
import { Stroke } from 'ol/style';
import { Style } from 'ol/style';
import { I18nService } from '../../../i18n/i18n.service';
import { Layers } from './layers';
import { MapLayer } from './map-layer';

export class GpxLayer {
  constructor(private i18nService: I18nService) {}

  build(): MapLayer {
    const lineStyle = new Style({
      stroke: new Stroke({
        color: 'rgba(0, 0, 255, 0.3)',
        width: 15,
      }),
    });

    const style = {
      // eslint-disable-next-line @typescript-eslint/naming-convention
      LineString: lineStyle,
      // eslint-disable-next-line @typescript-eslint/naming-convention
      MultiLineString: lineStyle,
    };

    const layer = new VectorLayer({
      zIndex: Layers.zIndexGpxLayer,
      source: new VectorSource({
        features: [],
      }),
      style: (feature) => {
        return style[feature.getGeometry().getType()];
      },
    });

    const gpxLayerName = this.i18nService.translation('@@map.layer.gpx');
    layer.set('name', gpxLayerName);
    return new MapLayer('gpx-layer', layer);
  }
}
