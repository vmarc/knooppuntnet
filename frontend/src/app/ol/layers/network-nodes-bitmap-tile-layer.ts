import { NetworkType } from '@api/common';
import { Translations } from '@app/i18n';
import TileLayer from 'ol/layer/Tile';
import XYZ from 'ol/source/XYZ';
import { ZoomLevel } from '../domain';
import { OldMapLayer } from './old-map-layer';

export class NetworkNodesBitmapTileLayer {
  static build(networkType: NetworkType): OldMapLayer {
    const layer = new TileLayer({
      source: new XYZ({
        minZoom: ZoomLevel.bitmapTileMinZoom,
        maxZoom: ZoomLevel.bitmapTileMaxZoom,
        url: `/tiles/${networkType}/analysis/{z}/{x}/{y}.png`,
      }),
    });
    const name = Translations.get(`network-type.${networkType}`);
    return new OldMapLayer(
      `network-nodes-${networkType}-layer`,
      name,
      ZoomLevel.bitmapTileMinZoom,
      ZoomLevel.bitmapTileMaxZoom,
      'bitmap',
      layer
    );
  }
}
