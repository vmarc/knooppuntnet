import { Map as MaplibreMap } from 'maplibre-gl';

export class OpenDataSource {
  private static TILE_ROUTE_LAYER = 'opendata-route';
  private static TILE_NODE_LAYER = 'opendata-node';

  constructor(
    private map: MaplibreMap,
    private country: string,
    private routeType: string
  ) {
    this.initSource();
    this.initLayerRoute();
    this.initLayerVirtualRoute();
    this.initLayerNode();
    this.initLayerVirtualNode();
    this.initLayerNodeName();
  }

  updateVisibility(visible: boolean): void {
    this.updateLayerVisibility(this.layerIdRoute(), visible);
    this.updateLayerVisibility(this.layerIdVirtualRoute(), visible);
    this.updateLayerVisibility(this.layerIdNode(), visible);
    this.updateLayerVisibility(this.layerIdVirtualNode(), visible);
    this.updateLayerVisibility(this.layerIdNodeName(), visible);
  }

  private updateLayerVisibility(layerId: string, visible: boolean): void {
    this.map.setLayoutProperty(layerId, 'visibility', visible ? 'visible' : 'none');
  }

  remove(): void {
    this.map.removeLayer(this.layerIdRoute());
    this.map.removeLayer(this.layerIdVirtualRoute());
    this.map.removeLayer(this.layerIdNode());
    this.map.removeLayer(this.layerIdVirtualNode());
    this.map.removeLayer(this.layerIdNodeName());
    this.map.removeSource(this.sourceId());
  }

  private initSource(): void {
    this.map.addSource(this.sourceId(), {
      type: 'vector',
      tiles: [
        `http://localhost:4000/tiles/opendata/${this.country}/${this.routeType}/{z}/{x}/{y}.mvt`,
      ],
      maxzoom: 13,
      minzoom: 6,
    });
  }

  private initLayerRoute(): void {
    this.map.addLayer({
      id: this.layerIdRoute(),
      type: 'line',
      source: this.sourceId(),
      'source-layer': OpenDataSource.TILE_ROUTE_LAYER,
      filter: ['!=', 'virtual', 'true'],
      layout: {
        'line-join': 'round',
        'line-cap': 'round',
        visibility: 'none',
      },
      paint: {
        'line-color': '#ff0000',
        'line-width': ['interpolate', ['linear'], ['zoom'], 6, 1, 8, 2, 10, 3],
      },
    });
  }

  private initLayerVirtualRoute(): void {
    this.map.addLayer({
      id: this.layerIdVirtualRoute(),
      type: 'line',
      source: this.sourceId(),
      'source-layer': OpenDataSource.TILE_ROUTE_LAYER,
      filter: ['==', 'virtual', 'true'],
      layout: {
        'line-join': 'round',
        'line-cap': 'round',
        visibility: 'none',
      },
      paint: {
        'line-color': '#ff0000',
        'line-dasharray': [1, 2],
        'line-width': ['interpolate', ['linear'], ['zoom'], 6, 1, 10, 2],
      },
    });
  }

  private initLayerNode(): void {
    this.map.addLayer({
      id: this.layerIdNode(),
      type: 'circle',
      source: this.sourceId(),
      'source-layer': OpenDataSource.TILE_NODE_LAYER,
      filter: ['!=', 'virtual', 'true'],
      layout: {
        visibility: 'none',
      },
      paint: {
        'circle-radius': 10,
        'circle-color': '#ffffff',
        'circle-stroke-width': 3,
        'circle-stroke-color': '#ff0000',
      },
    });
  }

  private initLayerNodeName(): void {
    this.map.addLayer({
      id: this.layerIdNodeName(),
      type: 'symbol',
      source: this.sourceId(),
      'source-layer': OpenDataSource.TILE_NODE_LAYER,
      layout: {
        'text-field': ['get', 'name'],
        //'text-font': ['Roboto Regular'],
        'text-size': 12,
        'text-anchor': 'center',
        visibility: 'none',
      },
    });
  }

  private initLayerVirtualNode(): void {
    this.map.addLayer({
      id: this.layerIdVirtualNode(),
      type: 'circle',
      source: this.sourceId(),
      'source-layer': OpenDataSource.TILE_NODE_LAYER,
      filter: ['==', 'virtual', 'true'],
      layout: {
        visibility: 'none',
      },
      paint: {
        'circle-radius': 10,
        'circle-color': '#ffffff',
        'circle-stroke-width': 1,
        'circle-stroke-color': '#ff0000',
      },
    });
  }

  private sourceId(): string {
    return `opendata-${this.country}-${this.routeType}`;
  }

  private layerIdRoute(): string {
    return `opendata-${this.country}-${this.routeType}-route`;
  }

  private layerIdNode(): string {
    return `opendata-${this.country}-${this.routeType}-node`;
  }

  private layerIdNodeName(): string {
    return `opendata-${this.country}-${this.routeType}-nodename`;
  }

  private layerIdVirtualRoute(): string {
    return `opendata-${this.country}-${this.routeType}-virtual-route`;
  }

  private layerIdVirtualNode(): string {
    return `opendata-${this.country}-${this.routeType}-virtual-node`;
  }
}
