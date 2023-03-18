import { Store } from '@ngrx/store';
import LayerGroup from 'ol/layer/Group';
import VectorTileLayer from 'ol/layer/VectorTile';
import Map from 'ol/Map';
import { ZoomLevel } from '../domain/zoom-level';
import { MapService } from '../services/map.service';
import { MainMapStyle } from '../style/main-map-style';
import { MapLayer } from './map-layer';
import { NetworkBitmapTileLayer } from './network-bitmap-tile-layer';
import { NetworkVectorTileLayer } from './network-vector-tile-layer';

export class MainMapLayer {
  bitmapTileLayer: MapLayer;
  vectorTileLayer: VectorTileLayer;

  constructor(private mapService: MapService, private store: Store) {}

  build(): MapLayer {
    const networkType = this.mapService.networkType();
    this.bitmapTileLayer = NetworkBitmapTileLayer.build(
      networkType,
      'analysis'
    );
    this.vectorTileLayer = NetworkVectorTileLayer.oldBuild(networkType);

    const layer = new LayerGroup({
      layers: [this.bitmapTileLayer.layer, this.vectorTileLayer],
    });
    // TODO need to unsubscribe
    this.mapService.mapMode$.subscribe(() =>
      this.vectorTileLayer.getSource().changed()
    );
    return new MapLayer(networkType, layer, networkType, null, this.applyMap());
  }

  private applyMap() {
    return (map: Map) => {
      const mainMapStyle = new MainMapStyle(
        map,
        this.mapService,
        this.store
      ).styleFunction();
      this.vectorTileLayer.setStyle(mainMapStyle);
      this.updateLayerVisibility(map.getView().getZoom());
      // TODO need to unsubscribe
      map
        .getView()
        .on('change:resolution', () => this.zoom(map.getView().getZoom()));
    };
  }

  private zoom(zoomLevel: number) {
    this.updateLayerVisibility(zoomLevel);
    return true;
  }

  private updateLayerVisibility(zoomLevel: number) {
    const zoom = Math.round(zoomLevel);
    if (zoom <= ZoomLevel.bitmapTileMaxZoom) {
      this.bitmapTileLayer.layer.setVisible(true);
      this.vectorTileLayer.setVisible(false);
    } else if (zoom >= ZoomLevel.vectorTileMinZoom) {
      this.bitmapTileLayer.layer.setVisible(false);
      this.vectorTileLayer.setVisible(true);
    }
  }
}
