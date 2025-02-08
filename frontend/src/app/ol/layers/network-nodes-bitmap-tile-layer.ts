import { RouteType } from '@api/common/route-type';
import { Translations } from '@app/i18n';
import TileLayer from 'ol/layer/Tile';
import XYZ from 'ol/source/XYZ';
import { ZoomLevel } from '../domain/zoom-level';
import { OldMapLayer } from './old-map-layer';

export class NetworkNodesBitmapTileLayer {
  static build(routeType: RouteType): OldMapLayer {
    const layer = new TileLayer({
      source: new XYZ({
        minZoom: ZoomLevel.bitmapTileMinZoom,
        maxZoom: ZoomLevel.bitmapTileMaxZoom,
        url: `/tiles/${routeType}/analysis/{z}/{x}/{y}.png`,
      }),
    });
    const name = Translations.get(`route-type.${routeType}`);
    return new OldMapLayer(
      `network-nodes-${routeType}-layer`,
      name,
      ZoomLevel.bitmapTileMinZoom,
      ZoomLevel.bitmapTileMaxZoom,
      'bitmap',
      layer
    );
  }
}
