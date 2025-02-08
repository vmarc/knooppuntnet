import { RouteType } from '@api/common/route-type';
import { Translations } from '@app/i18n';
import { OldMapLayer } from '@app/ol/layers/old-map-layer';
import TileLayer from 'ol/layer/Tile';
import XYZ from 'ol/source/XYZ';
import { ZoomLevel } from '../domain/zoom-level';
import { MapMode } from '../services/map-mode';

export class NetworkBitmapTileLayer {
  public static build(routeType: RouteType, mapMode: MapMode): OldMapLayer {
    const name = Translations.get(`route-type.${routeType}`);
    return new OldMapLayer(
      routeType,
      name,
      ZoomLevel.bitmapTileMinZoom,
      ZoomLevel.bitmapTileMaxZoom,
      'bitmap',
      new TileLayer({
        source: new XYZ({
          minZoom: ZoomLevel.bitmapTileMinZoom,
          maxZoom: ZoomLevel.bitmapTileMaxZoom,
          url: `/tiles/${routeType}/${mapMode}/{z}/{x}/{y}.png`,
        }),
      }),
      routeType,
      mapMode
    );
  }
}
