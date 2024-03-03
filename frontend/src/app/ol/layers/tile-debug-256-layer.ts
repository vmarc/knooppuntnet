import TileLayer from 'ol/layer/Tile';
import TileDebug from 'ol/source/TileDebug';
import { createXYZ } from 'ol/tilegrid';
import { MapLayer } from './map-layer';

export class TileDebug256Layer {
  static build(): MapLayer {
    const tileGrid = createXYZ({
      tileSize: 256, // <--
      maxZoom: 20,
    });

    const layer = new TileLayer({
      source: new TileDebug({
        // zDirection: 1,
        tileGrid,
      }),
    });

    const name = $localize`:@@map.layer.tile-256-names:Tilenames (256)`;
    return MapLayer.build('debug-256', name, layer);
  }
}
