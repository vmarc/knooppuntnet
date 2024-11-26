import TileLayer from 'ol/layer/Tile';
import OSM from 'ol/source/OSM';
import { OldMapLayer } from './old-map-layer';

export class OldOsmLayer {
  static id = 'osm';

  static build(): OldMapLayer {
    const layer = new TileLayer({
      source: new OSM({
        url: 'https://tile.openstreetmap.org/{z}/{x}/{y}.png',
      }),
    });
    const name = $localize`:@@map.layer.osm:OpenStreetMap`;
    return new OldMapLayer(OldOsmLayer.id, name, -Infinity, Infinity, 'bitmap', layer, null, null);
  }
}
