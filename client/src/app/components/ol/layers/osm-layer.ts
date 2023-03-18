import TileLayer from 'ol/layer/Tile';
import OSM from 'ol/source/OSM';
import { MapLayer } from './map-layer';

export class OsmLayer {
  build(): MapLayer {
    const layer = new TileLayer({
      source: new OSM({
        url: 'https://tile.openstreetmap.org/{z}/{x}/{y}.png',
      }),
    });
    return new MapLayer('osm', layer);
  }
}
