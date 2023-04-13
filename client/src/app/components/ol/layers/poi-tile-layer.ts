import { MVT } from 'ol/format';
import VectorTileLayer from 'ol/layer/VectorTile';
import VectorTile from 'ol/source/VectorTile';
import { ZoomLevel } from '../domain';
import { Layers } from './layers';

export class PoiTileLayer {
  build(): VectorTileLayer {
    const source = new VectorTile({
      tileSize: 512,
      minZoom: ZoomLevel.poiTileMinZoom,
      maxZoom: ZoomLevel.poiTileMaxZoom,
      format: new MVT(),
      url: '/tiles-history/poi/{z}/{x}/{y}.mvt',
    });

    return new VectorTileLayer({
      zIndex: Layers.zIndexPoiLayer,
      source,
      renderBuffer: 40,
      declutter: false,
      className: 'poi',
      renderMode: 'vector',
    });
  }
}
