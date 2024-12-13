import { NetworkType } from '@api/common';
import TileLayer from 'ol/layer/Tile';
import XYZ from 'ol/source/XYZ';
import { ZoomLevel } from '../domain';
import { OldMapLayer } from './old-map-layer';

export class OldOpendataBitmapTileLayer {
  static build(networkType: NetworkType, id: string, layerName: string, dir: string): OldMapLayer {
    const layer = new TileLayer<XYZ>({
      source: new XYZ({
        minZoom: ZoomLevel.bitmapTileMinZoom,
        maxZoom: ZoomLevel.bitmapTileMaxZoom,
        url: `/tiles/opendata/${dir}/{z}/{x}/{y}.png`,
      }),
    });

    return new OldMapLayer(
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
