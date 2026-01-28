import { Map as MaplibreMap } from 'maplibre-gl';
import { RouteTileLayerId } from '../constants/route-tile-layer-id';
import { SourceId } from '../constants/source-id';
import { MapLayerId } from '../constants/map-layer-id';

export class RouteSource {
  static init(map: MaplibreMap, routeType: string): void {
    this.initSource(map, routeType);
    this.initLayerNodeRoute(map);
    this.initLayerNodeRouteArrows(map);
    this.initLayerNode(map);
    this.initLayerNodeName(map);
  }

  static remove(map: MaplibreMap): void {
    map.removeLayer(MapLayerId.NODE_ROUTE);
    map.removeLayer(MapLayerId.NODE_ROUTE_ARROWS);
    map.removeLayer(MapLayerId.NODE);
    map.removeLayer(MapLayerId.NODE_NAME);
    map.removeSource(SourceId.ROUTES);
  }

  private static initSource(map: MaplibreMap, routeType: string): void {
    map.addSource(SourceId.ROUTES, {
      type: 'vector',
      tiles: [`http://localhost:4200/tiles/${routeType}/{z}/{x}/{y}.mvt`],
      maxzoom: 13,
    });
  }

  private static initLayerNodeRoute(map: MaplibreMap): void {
    map.addLayer({
      id: MapLayerId.NODE_ROUTE,
      type: 'line',
      source: 'routes',
      'source-layer': RouteTileLayerId.NODE_ROUTE,
      layout: {
        'line-join': 'round',
        'line-cap': 'round',
      },
      paint: {
        'line-color': '#ff69b4',
        'line-width': 3,
      },
    });
  }

  private static initLayerNodeRouteArrows(map: MaplibreMap): void {
    if (map.getImage('node-route-arrow') == null) {
      map.loadImage('/assets/arrow.png').then((response) => {
        map.addImage('node-route-arrow', response.data);
      });
    }

    map.addLayer({
      id: MapLayerId.NODE_ROUTE_ARROWS,
      type: 'symbol',
      source: 'routes',
      'source-layer': RouteTileLayerId.NODE_ROUTE,
      layout: {
        'symbol-placement': 'line',
        'icon-image': 'node-route-arrow',
        'icon-rotation-alignment': 'map',
        'icon-allow-overlap': true,
        'symbol-spacing': 10,
      },
    });
  }

  private static initLayerNode(map: MaplibreMap): void {
    map.addLayer({
      id: MapLayerId.NODE,
      type: 'circle',
      source: 'routes',
      'source-layer': RouteTileLayerId.NODE,
      paint: {
        'circle-radius': 10,
        'circle-color': '#ffffff',
        'circle-stroke-width': 3,
        'circle-stroke-color': '#ff69b4',
      },
    });
  }

  private static initLayerNodeName(map: MaplibreMap): void {
    map.addLayer({
      id: MapLayerId.NODE_NAME,
      type: 'symbol',
      source: 'routes',
      'source-layer': RouteTileLayerId.NODE,
      layout: {
        'text-field': ['get', 'ref'],
        //'text-font': ['Roboto Regular'],
        'text-size': 12,
        'text-anchor': 'center',
      },
    });
  }
}
