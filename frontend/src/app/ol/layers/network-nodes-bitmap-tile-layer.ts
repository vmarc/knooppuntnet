import { RouteType } from '@api/common/route-type';
import { Translations } from '@app/shared/i18n/translations';
import TileLayer from 'ol/layer/Tile';
import XYZ from 'ol/source/XYZ';
import { ZoomLevel } from '../domain/zoom-level';
import { OldOldMapLayer } from './old-old-map-layer';

export class NetworkNodesBitmapTileLayer {
  static build(routeType: RouteType): OldOldMapLayer {
    const layer = new TileLayer({
      source: new XYZ({
        minZoom: ZoomLevel.bitmapTileMinZoom,
        maxZoom: ZoomLevel.bitmapTileMaxZoom,
        url: `/tiles/${routeType}/analysis/{z}/{x}/{y}.png`,
      }),
    });
    const name = Translations.get(`route-type.${routeType}`);
    return new OldOldMapLayer(
      `network-nodes-${routeType}-layer`,
      name,
      ZoomLevel.bitmapTileMinZoom,
      ZoomLevel.bitmapTileMaxZoom,
      'bitmap',
      layer
    );
  }
}
