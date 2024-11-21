import TileLayer from 'ol/layer/Tile';
import OSM from 'ol/source/OSM';

export class OsmLayer {
  static build(): TileLayer<OSM> {
    return new TileLayer({
      source: new OSM({
        url: 'https://tile.openstreetmap.org/{z}/{x}/{y}.png',
      }),
    });
  }
}
