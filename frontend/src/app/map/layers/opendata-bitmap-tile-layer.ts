import { NetworkType } from '@api/custom';
import { ZoomLevel } from '@app/ol/domain';
import TileLayer from 'ol/layer/Tile';
import XYZ from 'ol/source/XYZ';
import { LayerType } from './layer-type';
import { MapLayer } from './map-layer';

export class OpendataBitmapTileLayer {
  static build(layerType: LayerType, networkType: NetworkType, dir: string): MapLayer {
    const layer = new TileLayer<XYZ>({
      source: new XYZ({
        tileSize: 256,
        minZoom: ZoomLevel.bitmapTileMinZoom,
        maxZoom: ZoomLevel.bitmapTileMaxZoom,
        url: `/tiles/opendata/${dir}/{z}/{x}/{y}.png`,
      }),
    });

    return {
      layerType,
      networkType,
      minZoom: ZoomLevel.bitmapTileMinZoom,
      maxZoom: ZoomLevel.bitmapTileMaxZoom,
      layer,
    };
  }
}
