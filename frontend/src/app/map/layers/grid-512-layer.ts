import TileLayer from 'ol/layer/Tile';
import TileDebug from 'ol/source/TileDebug';
import { createXYZ } from 'ol/tilegrid';

export class Grid512Layer {
  private static readonly tileGrid = createXYZ({
    tileSize: 512, // <--
    maxZoom: 20,
  });

  static build(): TileLayer<TileDebug> {
    return new TileLayer({
      source: new TileDebug({
        // zDirection: 1,
        tileGrid: this.tileGrid,
      }),
    });
  }
}
