import { Map as MaplibreMap } from 'maplibre-gl';

export class RouteSource {
  static TILE_LAYER_NODE = 'node';
  // TODO static TILE_LAYER_ERROR_NODE = 'error-node'; !!!
  static TILE_LAYER_NODE_ROUTE = 'node-route';
  static TILE_LAYER_ROUTE = 'route';

  constructor(
    private map: MaplibreMap,
    private routeType: string
  ) {
    this.initSource();
    this.initLayerRouteSurface();
    this.initLayerNodeRouteSurface();
    this.initLayerRoute();
    this.initLayerNodeRoute();
    this.initLayerNodeRouteArrows();
    this.initLayerNode();
    this.initLayerNodeName();
  }

  remove(): void {
    this.layerIds().forEach((layerId) => {
      if (this.map.getLayer(layerId)) {
        this.map.removeLayer(layerId);
      }
    });
    this.map.removeSource(this.sourceId());
  }

  private initSource(): void {
    this.map.addSource(this.sourceId(), {
      type: 'vector',
      tiles: [`http://localhost:4000/tiles/${this.routeType}/{z}/{x}/{y}.mvt`],
      maxzoom: 13,
      minzoom: 6,
    });
  }

  private initLayerRoute(): void {
    this.map.addLayer({
      id: this.layerIdRoute(),
      type: 'line',
      source: this.sourceId(),
      'source-layer': RouteSource.TILE_LAYER_ROUTE,
      layout: {
        visibility: 'none',
        'line-join': 'round',
        'line-cap': 'round',
      },
      paint: {
        'line-color': '#0000ff',
        'line-width': ['step', ['zoom'], 0.5, 10, 2, 12, 3],
      },
    });
  }

  private initLayerNodeRoute(): void {
    this.map.addLayer({
      id: this.layerIdNodeRoute(),
      type: 'line',
      source: this.sourceId(),
      'source-layer': RouteSource.TILE_LAYER_NODE_ROUTE,
      layout: {
        visibility: 'none',
        'line-join': 'round',
        'line-cap': 'round',
      },
      paint: {
        'line-color': '#0000ff',
        'line-width': ['step', ['zoom'], 0.5, 10, 2, 12, 3],
      },
    });
  }

  private initLayerNodeRouteArrows(): void {
    this.map.addLayer({
      id: this.layerIdNodeRouteArrow(),
      type: 'symbol',
      source: this.sourceId(),
      'source-layer': RouteSource.TILE_LAYER_NODE_ROUTE,
      layout: {
        visibility: 'none',
        'symbol-placement': 'line',
        'icon-image': 'node-route-arrow',
        'icon-rotation-alignment': 'map',
        'icon-allow-overlap': true,
        'symbol-spacing': 10,
      },
    });
  }

  private initLayerNode(): void {
    this.map.addLayer({
      id: this.layerIdNode(),
      type: 'circle',
      source: this.sourceId(),
      'source-layer': RouteSource.TILE_LAYER_NODE,
      minzoom: 12,
      layout: {
        visibility: 'none',
      },
      paint: {
        'circle-radius': 10,
        'circle-color': '#ffffff',
        'circle-stroke-color': '#0000ff',
        'circle-stroke-width': 2,
      },
    });
  }

  private initLayerNodeName(): void {
    this.map.addLayer({
      id: this.layerIdNodeName(),
      type: 'symbol',
      source: this.sourceId(),
      'source-layer': RouteSource.TILE_LAYER_NODE,
      layout: {
        visibility: 'none',
        'text-field': ['get', 'ref'],
        //'text-font': ['Roboto Regular'],
        'text-size': 12,
        'text-anchor': 'center',
      },
    });
  }

  private initLayerRouteSurface(): void {
    this.map.addLayer({
      id: this.layerIdRouteSurface(),
      type: 'line',
      source: this.sourceId(),
      'source-layer': RouteSource.TILE_LAYER_ROUTE,
      layout: {
        visibility: 'none',
        'line-join': 'round',
        'line-cap': 'round',
      },
      paint: {
        'line-color': [
          'match',
          ['get', 'surface'],
          'unpaved',
          'green',
          'unknown',
          'orange',
          'blue', // default
        ],
        'line-width': 3,
      },
    });
  }
  private initLayerNodeRouteSurface(): void {
    this.map.addLayer({
      id: this.layerIdNodeRouteSurface(),
      type: 'line',
      source: this.sourceId(),
      'source-layer': RouteSource.TILE_LAYER_NODE_ROUTE,
      layout: {
        visibility: 'none',
        'line-join': 'round',
        'line-cap': 'round',
      },
      paint: {
        'line-color': [
          'match',
          ['get', 'surface'],
          'unpaved',
          'green',
          'unknown',
          'orange',
          'blue', // default
        ],
        'line-width': 3,
      },
    });
  }

  private sourceId(): string {
    return `route-${this.routeType}`;
  }

  private layerIds(): string[] {
    return [
      this.layerIdRoute(),
      this.layerIdNodeRoute(),
      this.layerIdNodeRouteArrow(),
      this.layerIdNode(),
      this.layerIdNodeName(),
      this.layerIdRouteSurface(),
      this.layerIdNodeRouteSurface(),
    ];
  }

  private layerIdRoute(): string {
    return `route-${this.routeType}-route`;
  }

  private layerIdNodeRoute(): string {
    return `route-${this.routeType}-node-route`;
  }

  private layerIdNodeRouteArrow(): string {
    return `route-${this.routeType}-node-route-arrow`;
  }

  private layerIdNode(): string {
    return `route-${this.routeType}-node`;
  }

  private layerIdNodeName(): string {
    return `route-${this.routeType}-node-name`;
  }

  private layerIdRouteSurface(): string {
    return `route-${this.routeType}-route-surface`;
  }

  private layerIdNodeRouteSurface(): string {
    return `route-${this.routeType}-node-route-surface`;
  }
}
