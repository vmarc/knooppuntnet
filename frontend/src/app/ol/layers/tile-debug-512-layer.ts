import TileLayer from 'ol/layer/Tile';
import TileDebug from 'ol/source/TileDebug';
import { createXYZ } from 'ol/tilegrid';
import { OldMapLayer } from './old-map-layer';

export class TileDebug512Layer {
  static build(): OldMapLayer {
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

    const name = $localize`:@@map.layer.tile-512-names:Tilenames (512)`;
    return OldMapLayer.build('debug-512', name, layer);
  }
}
