import { GeoJSON } from 'ol/format';
import VectorLayer from 'ol/layer/Vector';
import VectorSource from 'ol/source/Vector';
import { Style } from 'ol/style';
import Icon from 'ol/style/Icon';
import { MapLayer } from './map-layer';

export class FrisoLayer {
  static build(name: string, filename: string): MapLayer {
    const vectorSource = new VectorSource({
      format: new GeoJSON(),
      url: `/tiles-history/routedatabank/${filename}`,
    });

    const src = `assets/images/marker-icon-red.png`;
    const style = new Style({
      image: new Icon({
        anchor: [12, 41],
        anchorXUnits: 'pixels',
        anchorYUnits: 'pixels',
        src,
      }),
    });

    const layer = new VectorLayer({
      source: vectorSource,
      style: (feature) => style,
    });

    return new MapLayer(
      `friso-${name}-layer`,
      name,
      -Infinity,
      Infinity,
      layer
    );
  }
}
