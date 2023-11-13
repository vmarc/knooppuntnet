import TileLayer from 'ol/layer/Tile';
import OSM from 'ol/source/OSM';
import { MapLayer } from './map-layer';

export class OsmLayer {
  static id = 'osm';

  static build(): MapLayer {
    const layer = new TileLayer({
      source: new OSM({
        url: 'https://tile.openstreetmap.org/{z}/{x}/{y}.png',
      }),
    });
    return MapLayer.simpleLayer(this.id, layer);
  }
}
