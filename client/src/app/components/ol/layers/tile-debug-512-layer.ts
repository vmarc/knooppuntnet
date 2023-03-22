import TileLayer from 'ol/layer/Tile';
import TileDebug from 'ol/source/TileDebug';
import { createXYZ } from 'ol/tilegrid';
import { MapLayer } from './map-layer';

export class TileDebug512Layer {
  public build(): MapLayer {
    const tileGrid = createXYZ({
      tileSize: 512, // <--
      maxZoom: 20,
    });

    const layer = new TileLayer({
      source: new TileDebug({
        // zDirection: 1,
        tileGrid,
      }),
    });

    return MapLayer.simpleLayer('debug-512', layer);
  }
}
