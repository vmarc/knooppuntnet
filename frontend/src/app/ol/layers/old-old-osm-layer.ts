import TileLayer from 'ol/layer/Tile';
import OSM from 'ol/source/OSM';
import { OldOldMapLayer } from './old-old-map-layer';

export class OldOldOsmLayer {
  static id = 'osm';

  static build(): OldOldMapLayer {
    const layer = new TileLayer({
      source: new OSM({
        url: 'https://tile.openstreetmap.org/{z}/{x}/{y}.png',
      }),
    });
    const name = $localize`:@@map.layer.osm:OpenStreetMap`;
    return new OldOldMapLayer(
      OldOldOsmLayer.id,
      name,
      -Infinity,
      Infinity,
      'bitmap',
      layer,
      null,
      null
    );
  }
}
