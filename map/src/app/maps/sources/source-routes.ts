import { Map as MaplibreMap } from 'maplibre-gl';
import { RouteTileLayers } from './route-tile-layers';

export class SourceRoutes {
  static init(map: MaplibreMap): void {
    map.addSource('routes', {
      type: 'vector',
      tiles: ['http://localhost:4200/tiles/hiking/{z}/{x}/{y}.mvt'],
    });
    map.addLayer({
      id: 'node-route',
      type: 'line',
      source: 'routes',
      'source-layer': RouteTileLayers.NODE_ROUTE,
      layout: {
        'line-join': 'round',
        'line-cap': 'round',
      },
      paint: {
        'line-color': '#ff69b4',
        'line-width': 3,
      },
    });

    map.loadImage('/assets/arrow.png').then((response) => {
      map.addImage('node-route-arrow', response.data);
    });

    map.addLayer({
      id: 'node-route-arrows',
      type: 'symbol',
      source: 'routes',
      'source-layer': RouteTileLayers.NODE_ROUTE,
      layout: {
        'symbol-placement': 'line',
        'icon-image': 'node-route-arrow',
        'icon-rotation-alignment': 'map',
        'icon-allow-overlap': true,
        'symbol-spacing': 10,
      },
    });

    map.addLayer({
      id: 'node',
      type: 'circle',
      source: 'routes',
      'source-layer': RouteTileLayers.NODE,
      paint: {
        'circle-radius': 10,
        'circle-color': '#ffffff',
        'circle-stroke-width': 3,
        'circle-stroke-color': '#ff69b4',
      },
    });

    map.addLayer({
      id: 'node-name',
      type: 'symbol',
      source: 'routes',
      'source-layer': RouteTileLayers.NODE,
      layout: {
        'text-field': ['get', 'ref'],
        //'text-font': ['Roboto Regular'],
        'text-size': 12,
        'text-anchor': 'center',
      },
    });
  }
}
