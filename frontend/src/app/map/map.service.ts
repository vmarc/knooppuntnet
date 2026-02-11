import { signal } from '@angular/core';
import { effect } from '@angular/core';
import { Injectable, inject } from '@angular/core';
import { Bounds } from '@api/common/bounds';
import { RouteType } from '@api/common/route-type';
import { MapBuilder } from '@app/map/map-builder';
import { RouteMapOptions } from '@app/map/sources/route-map-options';
import { RouteSourceIds } from '@app/map/sources/route-source-ids';
import { Sources } from '@app/map/sources/sources';
import { LngLatLike } from 'maplibre-gl';
import { LngLatBounds } from 'maplibre-gl';
import { Marker } from 'maplibre-gl';
import { Map as MaplibreMap } from 'maplibre-gl';
import { FilterSpecification } from '@maplibre/maplibre-gl-style-spec';
import { State } from '@app/state/state';

@Injectable()
export class MapService {
  private readonly state = inject(State);

  private _map = signal<MaplibreMap | null>(null);
  private _sources = signal<Sources | null>(null);

  private actions: (() => void)[] = [];

  constructor() {
    effect(() => {
      const m = this._map();
      const s = this._sources();
      const options = this.state.map.routeMapOptions();
      if (m && s && options) {
        // TODO apply initial state from query parameters and local storage
        this.updateRouteSources(options);
        const enabled = this.state.map.layers.backgroundLayerEnabled();
        this.updateBackgroundVisibility(enabled);
        this.actions.forEach((action) => action());
        this.actions = [];
      }
    });
  }

  init(): void {
    this._map.set(new MapBuilder().build());
    this.map.loadImage('/assets/arrow.png').then((response) => {
      this.map.addImage('node-route-arrow', response.data);
    });
    this.map.on('load', () => {
      this._sources.set(new Sources(this.map));
    });
  }

  destroy(): void {
    this.sources.remove(); // TODO investigate whether this is needed (or already in this.map.remove()?)
    this.map.remove();
  }

  execute(action: () => void): void {
    const m = this._map();
    const s = this._sources();
    const options = this.state.map.routeMapOptions();
    if (m && s) {
      this.updateRouteSources(options);
      action();
    } else {
      this.actions.push(action);
    }
  }

  updateRouteSources(options: RouteMapOptions): void {
    this.sources.updateRouteSources(options);
  }

  fitBounds(bounds: Bounds): void {
    const inset = 0.15;
    const lonDelta = (bounds.maxLon - bounds.minLon) * inset;
    const latDelta = (bounds.maxLat - bounds.minLat) * inset;
    const minLon = bounds.minLon - lonDelta;
    const minLat = bounds.minLat - latDelta;
    const maxLon = bounds.maxLon + lonDelta;
    const maxLat = bounds.maxLat + latDelta;
    const b = new LngLatBounds([minLon, minLat, maxLon, maxLat]);
    this.map.fitBounds(b);
  }

  zoomTo(zoom: number, center: LngLatLike): void {
    this.map.setZoom(zoom);
    this.map.setCenter(center);
  }

  addMarker(marker: Marker): void {
    marker.addTo(this.map);
  }

  selectRoutes(routeType: RouteType, routeIds: string[]): void {
    const filter: FilterSpecification = ['in', ['get', 'routeId'], ['literal', routeIds]];
    this.filterRoutes(routeType, filter);
  }

  resetRouteSelection(routeType: RouteType): void {
    this.filterRoutes(routeType, null);
  }

  nodeFocus(routeType: RouteType, nodeIds: string[]): void {
    this.updateVisibility(new RouteSourceIds(routeType).nodeFocusLayerId(), true);
    const filter: FilterSpecification = ['in', ['get', 'id'], ['literal', nodeIds]];
    this.filterNodes(routeType, filter);
  }

  resetNodeFocus(routeType: RouteType): void {
    this.updateVisibility(new RouteSourceIds(routeType).nodeFocusLayerId(), false);
    this.filterNodes(routeType, null);
  }

  hideRouteLayer(): void {
    // this.map.setLayoutProperty(MapLayerId.ROUTE, 'visibility', 'none');
  }

  showRouteLayer(): void {
    // this.map.setLayoutProperty(MapLayerId.ROUTE, 'visibility', 'visible');
  }

  showLayer(layerId: string): void {
    this.map.setLayoutProperty(layerId, 'visibility', 'visible');
  }

  hideLayer(layerId: string): void {
    this.map.setLayoutProperty(layerId, 'visibility', 'none');
  }

  private updateBackgroundVisibility(visible: boolean): void {
    const layers = this.map.getStyle().layers;
    layers.forEach((layer) => {
      if (layer['source'] === 'openmaptiles' || layer.id == 'background') {
        this.updateVisibility(layer.id, visible);
      }
    });
  }

  private updateVisibility(layerId: string, visble: boolean): void {
    this.map.setLayoutProperty(layerId, 'visibility', visble ? 'visible' : 'none');
  }

  private filterRoutes(routeType: RouteType, filter: FilterSpecification | null): void {
    const layerId = new RouteSourceIds(routeType).nodeRouteLayerId();
    console.log('filterRoutes', layerId, filter);
    this.map.setFilter(layerId, filter);
    // this.map.setFilter(MapLayerId.NODE_ROUTE_ARROWS, filter);
  }

  private filterNodes(routeType: RouteType, filter: FilterSpecification | null): void {
    const nodeLayerId = new RouteSourceIds(routeType).nodeFocusLayerId();
    this.map.setFilter(nodeLayerId, filter);
  }

  private get map(): MaplibreMap {
    if (!this._map()) {
      console.error('map not initialized while trying to access map');
    }
    return this._map();
  }

  private get sources(): Sources {
    if (!this._sources()) {
      console.error('map not initialized while trying to access sources');
    }
    return this._sources();
  }
}
