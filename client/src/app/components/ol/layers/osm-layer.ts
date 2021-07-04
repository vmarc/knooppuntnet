import { I18nService } from '../../../i18n/i18n.service';
import { MapLayer } from './map-layer';
import OSM from 'ol/source/OSM';
import TileLayer from 'ol/layer/Tile';

export class OsmLayer {
  constructor(private i18nService: I18nService) {}

  build(): MapLayer {
    const layer = new TileLayer({
      source: new OSM(),
    });

    const osmLayerName = this.i18nService.translation('@@map.layer.osm');
    layer.set('name', osmLayerName);
    layer.setVisible(false);
    return new MapLayer('osm-layer', layer);
  }
}
