import TileLayer from 'ol/layer/Tile';
import TileDebug from 'ol/source/TileDebug';
import {createXYZ} from 'ol/tilegrid';
import {I18nService} from '../../../i18n/i18n.service';
import {MapLayer} from './map-layer';

export class TileDebug512Layer {

  constructor(private i18nService: I18nService) {
  }

  public build(): MapLayer {

    const tileGrid = createXYZ({
      tileSize: 512, // <--
      maxZoom: 20
    });

    const layer = new TileLayer({
      source: new TileDebug({
        // zDirection: 1,
        tileGrid
      })
    });

    const layerName = this.i18nService.translation('@@map.layer.tile-512-names');
    layer.set('name', layerName);
    layer.setVisible(false);
    return new MapLayer('debug-layer', layer);
  }

}
