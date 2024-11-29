import TileLayer from 'ol/layer/Tile';
import TileDebug from 'ol/source/TileDebug';
import { createXYZ } from 'ol/tilegrid';
import { MapLayer } from './map-layer';

export class GridLayer {
  private static readonly tileGrid = createXYZ({
    tileSize: 256, // <--
    maxZoom: 20,
  });

  static build(): MapLayer {
    const layer = new TileLayer({
      source: new TileDebug({
        // zDirection: 1,
        tileGrid: this.tileGrid,
      }),
    });
    return {
      layerType: 'grid',
      minZoom: 2,
      maxZoom: 20,
      layer: layer,
    };
  }
}
