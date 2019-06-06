import MVT from 'ol/format/MVT.js';
import XYZ from 'ol/source/XYZ';
import VectorTileSource from 'ol/source/VectorTile.js';
import TileLayer from 'ol/layer/Tile.js';
import VectorTileLayer from 'ol/layer/VectorTile.js';
import Feature from 'ol/Feature';

export function createWalkingPngLayer() {
  return new TileLayer({
    source: new XYZ({url: 'http://127.0.0.1:8900/rwn/{z}/{x}/{y}.png'}),
  })
}

export function createWalkingMvtLayer() {
  return new VectorTileLayer({
    source: new VectorTileSource({
      url: 'http://127.0.0.1:8900/rwn/{z}/{x}/{y}.mvt',
      format: new MVT({featureClass: Feature})
    }),
    renderMode: 'image',
    zIndex: 25
  })
}



