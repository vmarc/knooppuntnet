import { NetworkType } from '@api/custom';
import { Translations } from '@app/i18n';
import TileLayer from 'ol/layer/Tile';
import XYZ from 'ol/source/XYZ';
import { MapLayer } from '.';
import { ZoomLevel } from '../domain';
import { MapMode } from '../services';

export class NetworkBitmapTileLayer {
  public static build(networkType: NetworkType, mapMode: MapMode): MapLayer {
    const name = Translations.get(`network-type.${networkType}`);
    return new MapLayer(
      networkType,
      name,
      ZoomLevel.bitmapTileMinZoom,
      ZoomLevel.bitmapTileMaxZoom,
      'bitmap',
      new TileLayer({
        source: new XYZ({
          minZoom: ZoomLevel.bitmapTileMinZoom,
          maxZoom: ZoomLevel.bitmapTileMaxZoom,
          url: `/tiles-history/${networkType}/${mapMode}/{z}/{x}/{y}.png`,
        }),
      }),
      networkType,
      mapMode
    );
  }
}
