import { SegmentColors } from '@app/mapold/domain/segment-colors';
import { ColorSpecification } from '@maplibre/maplibre-gl-style-spec';
import { DataDrivenPropertyValueSpecification } from '@maplibre/maplibre-gl-style-spec';
import { RouteSourceIds } from './route-source-ids';
import { Map as MaplibreMap } from 'maplibre-gl';

export class RouteSource {
  static TILE_LAYER_NODE = 'node';
  // TODO static TILE_LAYER_ERROR_NODE = 'error-node'; !!!
  static TILE_LAYER_NODE_ROUTE = 'node-route';
  static TILE_LAYER_ROUTE = 'route';

  private ids: RouteSourceIds;

  constructor(
    private map: MaplibreMap,
    private routeType: string
  ) {
    this.ids = new RouteSourceIds(routeType);
    this.initSource();
    this.initLayerInternational();
    this.initLayerNational();
    this.initLayerRegional();
    this.initLayerLocal();
    this.initLayerNodeFocus();
    this.initLayerRouteSurface();
    this.initLayerNodeRouteSurface();
    this.initLayerRoute();
    this.initLayerNodeRoute();
    this.initLayerNodeRouteSegment();
    this.initLayerNodeRoutePath();
    this.initLayerNodeRouteArrows();
    this.initLayerNode();
    this.initLayerNodeName();
  }

  remove(): void {
    this.ids.layerIds().forEach((layerId) => {
      if (this.map.getLayer(layerId)) {
        this.map.removeLayer(layerId);
      }
    });
    this.map.removeSource(this.ids.sourceId());
  }

  private initSource(): void {
    this.map.addSource(this.ids.sourceId(), {
      type: 'vector',
      tiles: [`http://localhost:4000/tiles/${this.routeType}/{z}/{x}/{y}.mvt`],
      maxzoom: 13,
      minzoom: 2,
    });
  }

  private initLayerRoute(): void {
    this.map.addLayer({
      id: this.ids.routeLayerId(),
      type: 'line',
      source: this.ids.sourceId(),
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
      id: this.ids.nodeRouteLayerId(),
      type: 'line',
      source: this.ids.sourceId(),
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

  private initLayerNodeRouteSegment(): void {
    const lineColorArray = ['match', ['get', 'segmentId']];

    const colors = SegmentColors.colors;
    for (let i = 0; i < colors.length; i++) {
      lineColorArray.push((i + 1).toString());
      lineColorArray.push(colors[i]);
    }
    lineColorArray.push('#eeeeee');

    this.map.addLayer({
      id: this.ids.nodeRouteSegmentLayerId(),
      type: 'line',
      source: this.ids.sourceId(),
      'source-layer': RouteSource.TILE_LAYER_NODE_ROUTE,
      layout: {
        visibility: 'none',
        'line-join': 'round',
        'line-cap': 'round',
      },
      paint: {
        'line-color': lineColorArray as DataDrivenPropertyValueSpecification<ColorSpecification>,
        'line-width': ['step', ['zoom'], 0.5, 10, 2, 12, 3],
      },
    });
  }

  private initLayerNodeRoutePath(): void {
    const lineColorArray = ['match', ['get', 'segmentElementId']];

    const colors = SegmentColors.colors;
    for (let i = 0; i < colors.length; i++) {
      lineColorArray.push((i + 1).toString());
      lineColorArray.push(colors[i]);
    }
    lineColorArray.push('#eeeeee');

    this.map.addLayer({
      id: this.ids.nodeRoutePathLayerId(),
      type: 'line',
      source: this.ids.sourceId(),
      'source-layer': RouteSource.TILE_LAYER_NODE_ROUTE,
      layout: {
        visibility: 'none',
        'line-join': 'round',
        'line-cap': 'round',
      },
      paint: {
        'line-color': lineColorArray as DataDrivenPropertyValueSpecification<ColorSpecification>,
        'line-width': ['step', ['zoom'], 0.5, 10, 2, 12, 3],
      },
    });
  }

  private initLayerNodeRouteArrows(): void {
    this.map.addLayer({
      id: this.ids.nodeRouteArrowLayerId(),
      type: 'symbol',
      source: this.ids.sourceId(),
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
      id: this.ids.nodeLayerId(),
      type: 'circle',
      source: this.ids.sourceId(),
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

  private initLayerNodeFocus(): void {
    this.map.addLayer({
      id: this.ids.nodeFocusLayerId(),
      type: 'circle',
      source: this.ids.sourceId(),
      'source-layer': RouteSource.TILE_LAYER_NODE,
      minzoom: 12,
      layout: {
        visibility: 'none',
      },
      paint: {
        'circle-radius': 16,
        'circle-color': '#ffff00',
        'circle-stroke-color': '#ffff00',
        'circle-stroke-width': 1,
      },
    });
  }

  private initLayerNodeName(): void {
    this.map.addLayer({
      id: this.ids.nodeNameLayerId(),
      type: 'symbol',
      source: this.ids.sourceId(),
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
      id: this.ids.routeSurfaceLayerId(),
      type: 'line',
      source: this.ids.sourceId(),
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
      id: this.ids.nodeRouteSurfaceLayerId(),
      type: 'line',
      source: this.ids.sourceId(),
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

  private initLayerInternational(): void {
    this.map.addLayer({
      id: this.ids.internationalLayerId(),
      type: 'line',
      source: this.ids.sourceId(),
      'source-layer': RouteSource.TILE_LAYER_ROUTE,
      filter: ['==', ['get', 'scope'], 'international'],
      layout: {
        visibility: 'none',
        'line-join': 'round',
        'line-cap': 'round',
      },
      paint: {
        'line-color': '#ff0000',
        'line-width': ['interpolate', ['linear'], ['zoom'], 2, 1, 6, 2, 10, 3, 12, 7],
      },
    });
  }

  private initLayerNational(): void {
    this.map.addLayer({
      id: this.ids.nationalLayerId(),
      type: 'line',
      source: this.ids.sourceId(),
      'source-layer': RouteSource.TILE_LAYER_ROUTE,
      filter: ['==', ['get', 'scope'], 'national'],
      layout: {
        visibility: 'none',
        'line-join': 'round',
        'line-cap': 'round',
      },
      paint: {
        'line-color': '#0000ff',
        'line-width': ['interpolate', ['linear'], ['zoom'], 2, 1, 7, 1, 9, 2, 11, 4],
      },
    });
  }

  private initLayerRegional(): void {
    this.map.addLayer({
      id: this.ids.regionalLayerId(),
      type: 'line',
      source: this.ids.sourceId(),
      'source-layer': RouteSource.TILE_LAYER_ROUTE,
      filter: ['==', ['get', 'scope'], 'regional'],
      layout: {
        visibility: 'none',
        'line-join': 'round',
        'line-cap': 'round',
      },
      paint: {
        'line-color': '#00cc00',
        'line-width': ['interpolate', ['linear'], ['zoom'], 2, 1, 9, 1, 11, 4],
      },
    });
  }

  private initLayerLocal(): void {
    this.map.addLayer({
      id: this.ids.localLayerId(),
      type: 'line',
      source: this.ids.sourceId(),
      'source-layer': RouteSource.TILE_LAYER_ROUTE,
      filter: ['==', ['get', 'scope'], 'local'],
      layout: {
        visibility: 'none',
        'line-join': 'round',
        'line-cap': 'round',
      },
      paint: {
        'line-color': '#ff8800',
        'line-width': 3,
      },
    });
  }
}
