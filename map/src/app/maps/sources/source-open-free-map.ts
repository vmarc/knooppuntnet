import { Map as MaplibreMap } from 'maplibre-gl';

export class SourceOpenFreeMap {
  static init(map: MaplibreMap): void {
    map.addSource('open-free-map', {
      type: 'vector',
      tiles: ['https://tiles.openfreemap.org/styles/liberty'],
    });
  }
}
