import { NetworkType } from '@api/custom/network-type';
import { ZoomLevel } from '@app/components/ol/domain/zoom-level';
import { Store } from '@ngrx/store';
import VectorTileLayer from 'ol/layer/VectorTile';
import Map from 'ol/Map';
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
    this.vectorTileLayer = NetworkVectorTileLayer.oldBuild(networkType);
    return new MapLayer(
      networkType,
      `${networkType}-vector`,
      ZoomLevel.vectorTileMinZoom - 1, // TODO planner: suspicious!!!
      ZoomLevel.vectorTileMaxOverZoom,
      this.vectorTileLayer,
      networkType,
      null,
      this.applyMap()
    );
  }

  buildBitmapLayer(networkType: NetworkType): MapLayer {
    return NetworkBitmapTileLayer.build(networkType, 'analysis');
  }

  private applyMap() {
    return (map: Map) => {
      const mainMapStyle = new MainMapStyle(
        map,
        this.mapService,
        this.store
      ).styleFunction();
      this.vectorTileLayer.setStyle(mainMapStyle);
    };
  }
}
