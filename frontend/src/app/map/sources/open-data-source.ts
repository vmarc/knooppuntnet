import { OpenDataSourceIds } from '@app/map/sources/open-data-source-ids';
import { Map as MaplibreMap } from 'maplibre-gl';

export class OpenDataSource {
  private static TILE_ROUTE_LAYER = 'opendata-route';
  private static TILE_NODE_LAYER = 'opendata-node';

  private ids: OpenDataSourceIds;

  constructor(
    private map: MaplibreMap,
    private country: string,
    private routeType: string
  ) {
    this.ids = new OpenDataSourceIds(country, routeType);
    this.initSource();
    this.initLayerRoute();
    this.initLayerVirtualRoute();
    this.initLayerNode();
    this.initLayerVirtualNode();
    this.initLayerNodeName();
  }

  updateVisibility(visible: boolean): void {
    this.updateLayerVisibility(this.ids.routeLayerId(), visible);
    this.updateLayerVisibility(this.ids.virtualRouteLayerId(), visible);
    this.updateLayerVisibility(this.ids.nodeLayerId(), visible);
    this.updateLayerVisibility(this.ids.virtualNodeLayerId(), visible);
    this.updateLayerVisibility(this.ids.nodeNameLayerId(), visible);
  }

  private updateLayerVisibility(layerId: string, visible: boolean): void {
    this.map.setLayoutProperty(layerId, 'visibility', visible ? 'visible' : 'none');
  }

  remove(): void {
    this.map.removeLayer(this.ids.routeLayerId());
    this.map.removeLayer(this.ids.virtualRouteLayerId());
    this.map.removeLayer(this.ids.nodeLayerId());
    this.map.removeLayer(this.ids.virtualNodeLayerId());
    this.map.removeLayer(this.ids.nodeNameLayerId());
    this.map.removeSource(this.ids.sourceId());
  }

  private initSource(): void {
    this.map.addSource(this.ids.sourceId(), {
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
      id: this.ids.routeLayerId(),
      type: 'line',
      source: this.ids.sourceId(),
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
      id: this.ids.virtualRouteLayerId(),
      type: 'line',
      source: this.ids.sourceId(),
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
      id: this.ids.nodeLayerId(),
      type: 'circle',
      source: this.ids.sourceId(),
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
      id: this.ids.nodeNameLayerId(),
      type: 'symbol',
      source: this.ids.sourceId(),
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
      id: this.ids.virtualNodeLayerId(),
      type: 'circle',
      source: this.ids.sourceId(),
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
}
