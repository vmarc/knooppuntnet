import { NetworkType } from '@api/custom/network-type';
import TileLayer from 'ol/layer/Tile';
import XYZ from 'ol/source/XYZ';
import { ZoomLevel } from '../domain/zoom-level';
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
    return new MapLayer(
      `network-nodes-${networkType}-layer`,
      `network-nodes-${networkType}-layer`,
      ZoomLevel.bitmapTileMinZoom,
      ZoomLevel.bitmapTileMaxZoom,
      layer,
      networkType,
      null
    );
  }
}
