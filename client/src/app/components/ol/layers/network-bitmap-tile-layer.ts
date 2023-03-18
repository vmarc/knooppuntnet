import { NetworkType } from '@api/custom/network-type';
import TileLayer from 'ol/layer/Tile';
import XYZ from 'ol/source/XYZ';
import { ZoomLevel } from '../domain/zoom-level';
import { MapMode } from '../services/map-mode';
import { MapLayer } from './map-layer';

export class NetworkBitmapTileLayer {
  public static build(networkType: NetworkType, mapType: MapMode): MapLayer {
    return new MapLayer(
      networkType,
      new TileLayer({
        source: new XYZ({
          minZoom: ZoomLevel.bitmapTileMinZoom,
          maxZoom: ZoomLevel.bitmapTileMaxZoom,
          url: `/tiles-history/${networkType}/${mapType}/{z}/{x}/{y}.png`,
        }),
      }),
      networkType,
      mapType
    );
  }
}
