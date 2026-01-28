import { RouteType } from '@api/common/route-type';
import TileLayer from 'ol/layer/Tile';
import XYZ from 'ol/source/XYZ';
import { ZoomLevel } from '../domain/zoom-level';
import { OldOldMapLayer } from './old-old-map-layer';

export class OldOldOpendataBitmapTileLayer {
  static build(routeType: RouteType, id: string, layerName: string, dir: string): OldOldMapLayer {
    const layer = new TileLayer<XYZ>({
      source: new XYZ({
        minZoom: ZoomLevel.bitmapTileMinZoom,
        maxZoom: ZoomLevel.bitmapTileMaxZoom,
        url: `/tiles/opendata/${dir}/{z}/{x}/{y}.png`,
      }),
    });

    return new OldOldMapLayer(
      id,
      layerName,
      ZoomLevel.bitmapTileMinZoom,
      ZoomLevel.bitmapTileMaxZoom,
      'bitmap',
      layer,
      routeType,
      null
    );
  }
}
