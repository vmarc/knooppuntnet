import { GeoJSON } from 'ol/format';
import VectorLayer from 'ol/layer/Vector';
import VectorSource from 'ol/source/Vector';
import { Style } from 'ol/style';
import Icon from 'ol/style/Icon';
import IconAnchorUnits from 'ol/style/IconAnchorUnits';
import { MapLayer } from './map-layer';

export class FrisoLayer {
  constructor(private name: string, private filename: string) {}

  build(): MapLayer {
    const vectorSource = new VectorSource({
      format: new GeoJSON(),
      url: `/tiles-history/routedatabank/${this.filename}`,
    });

    const src = `assets/images/marker-icon-red.png`;
    const style = new Style({
      image: new Icon({
        anchor: [12, 41],
        anchorXUnits: IconAnchorUnits.PIXELS,
        anchorYUnits: IconAnchorUnits.PIXELS,
        src,
      }),
    });

    const layer = new VectorLayer({
      source: vectorSource,
      style: (feature) => style,
    });

    return MapLayer.simpleLayer(`friso-${this.name}-layer`, layer);
  }
}
