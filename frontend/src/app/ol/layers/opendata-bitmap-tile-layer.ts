import { NetworkType } from '@api/custom';
import TileLayer from 'ol/layer/Tile';
import XYZ from 'ol/source/XYZ';
import { ZoomLevel } from '../domain';
import { MapLayer } from './map-layer';

export class OpendataBitmapTileLayer {
  static build(networkType: NetworkType, id: string, layerName: string, dir: string): MapLayer {
    const layer = new TileLayer<XYZ>({
      source: new XYZ({
        minZoom: ZoomLevel.bitmapTileMinZoom,
        maxZoom: ZoomLevel.bitmapTileMaxZoom,
        url: `/tiles-history/opendata/${dir}/{z}/{x}/{y}.png`,
      }),
    });

    return new MapLayer(
      id,
      layerName,
      ZoomLevel.bitmapTileMinZoom,
      ZoomLevel.bitmapTileMaxZoom,
      'bitmap',
      layer,
      networkType,
      null
    );
  }
}
