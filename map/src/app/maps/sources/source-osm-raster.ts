import { Map as MaplibreMap } from 'maplibre-gl';

export class SourceOsmRaster {
  static init(map: MaplibreMap): void {
    map.addSource('osm-raster', {
      type: 'raster',
      tiles: ['https://tile.openstreetmap.org/{z}/{x}/{y}.png'],
      tileSize: 256,
      minzoom: 0,
      maxzoom: 19,
    });
    map.addLayer({
      id: 'simple-tiles',
      type: 'raster',
      source: 'osm-raster',
    });
  }
}
