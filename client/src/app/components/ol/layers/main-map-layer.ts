import { NetworkType } from '@api/custom/network-type';
import { ZoomLevel } from '@app/components/ol/domain/zoom-level';
import { Store } from '@ngrx/store';
import VectorTileLayer from 'ol/layer/VectorTile';
import { MapService } from '../services/map.service';
import { MainMapStyle } from '../style/main-map-style';
import { MapLayer } from './map-layer';
import { NetworkBitmapTileLayer } from './network-bitmap-tile-layer';
import { NetworkVectorTileLayer } from './network-vector-tile-layer';

export class MainMapLayer {
  bitmapTileLayer: MapLayer;
  vectorTileLayer: VectorTileLayer;

  constructor(private mapService: MapService, private store: Store) {}

  buildVectorLayer(networkType: NetworkType): MapLayer {
    const layer = NetworkVectorTileLayer.oldBuild(networkType);
    const mainMapStyle = new MainMapStyle(
      this.mapService,
      this.store
    ).styleFunction();
    layer.setStyle(mainMapStyle);
    return new MapLayer(
      networkType,
      `${networkType}-vector`,
      ZoomLevel.vectorTileMinZoom - 1, // TODO planner: suspicious!!!
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
