import { NetworkType } from '@api/custom';
import { Translations } from '@app/i18n';
import TileLayer from 'ol/layer/Tile';
import XYZ from 'ol/source/XYZ';
import { ZoomLevel } from '../domain';
import { MapLayer } from './map-layer';

export class NetworkNodesBitmapTileLayer {
  static build(networkType: NetworkType): MapLayer {
    const layer = new TileLayer({
      source: new XYZ({
        minZoom: ZoomLevel.bitmapTileMinZoom,
        maxZoom: ZoomLevel.bitmapTileMaxZoom,
        url: `/tiles-history/${networkType}/analysis/{z}/{x}/{y}.png`,
      }),
    });
    const name = Translations.get(`network-type.${networkType}`);
    return new MapLayer(
      `network-nodes-${networkType}-layer`,
      name,
      ZoomLevel.bitmapTileMinZoom,
      ZoomLevel.bitmapTileMaxZoom,
      'bitmap',
      layer
    );
  }
}
