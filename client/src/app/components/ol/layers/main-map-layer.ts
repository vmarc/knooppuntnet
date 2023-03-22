import { NetworkType } from '@api/custom/network-type';
import { ZoomLevel } from '@app/components/ol/domain/zoom-level';
import { Store } from '@ngrx/store';
import { StyleFunction } from 'ol/style/Style';
import { MapService } from '../services/map.service';
import { MapLayer } from './map-layer';
import { NetworkBitmapTileLayer } from './network-bitmap-tile-layer';
import { NetworkVectorTileLayer } from './network-vector-tile-layer';

export class MainMapLayer {
  constructor(private mapService: MapService, private store: Store) {}

  buildVectorLayer(
    networkType: NetworkType,
    styleFunction: StyleFunction
  ): MapLayer {
    const layer = NetworkVectorTileLayer.oldBuild(networkType);
    layer.setStyle(styleFunction);
    return new MapLayer(
      networkType,
      `${networkType}-vector`,
      ZoomLevel.vectorTileMinZoom,
      ZoomLevel.vectorTileMaxOverZoom,
      layer,
      networkType,
      null
    );
  }

  buildBitmapLayer(networkType: NetworkType): MapLayer {
    return NetworkBitmapTileLayer.build(networkType, 'analysis');
  }
}
