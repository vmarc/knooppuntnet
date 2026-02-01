import { signal } from '@angular/core';
import { effect } from '@angular/core';
import { Injectable, inject } from '@angular/core';
import { Bounds } from '@api/common/bounds';
import { MapBuilder } from '@app/map/map-builder';
import { RouteSourceIds } from '@app/map/sources/route-source-ids';
import { Sources } from '@app/map/sources/sources';
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

  constructor() {
    effect(() => {
      const m = this._map();
      const s = this._sources();
      if (m && s) {
        // TODO apply initial state from query parameters and local storage
        const enabled = this.state.map.layers.backgroundLayerEnabled();
        this.updateBackgroundVisibility(enabled ? 'visible' : 'none');
        const layerId = new RouteSourceIds('hiking').nodeRouteLayerId();
        this.map.setLayoutProperty(layerId, 'visibility', 'visible');
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

  addMarker(marker: Marker): void {
    marker.addTo(this.map);
  }

  selectRoutes(routeIds: string[]): void {
    const filter: FilterSpecification = ['in', ['get', 'routeId'], ['literal', routeIds]];
    console.log('selectRoutes');
    this.filterRoutes(filter);
  }

  resetRouteSelection(): void {
    this.filterRoutes(null);
  }

  hideRouteLayer(): void {
    // this.map.setLayoutProperty(MapLayerId.ROUTE, 'visibility', 'none');
  }

  showRouteLayer(): void {
    // this.map.setLayoutProperty(MapLayerId.ROUTE, 'visibility', 'visible');
  }

  hideNodeRouteLayer(): void {
    // this.map.setLayoutProperty(MapLayerId.NODE_ROUTE, 'visibility', 'none');
    // this.map.setLayoutProperty(MapLayerId.NODE_ROUTE_ARROWS, 'visibility', 'none');
    // this.map.setLayoutProperty(MapLayerId.NODE, 'visibility', 'none');
    // this.map.setLayoutProperty(MapLayerId.NODE_NAME, 'visibility', 'none');
  }

  showNodeRouteLayer(): void {
    // this.map.setLayoutProperty(MapLayerId.NODE_ROUTE, 'visibility', 'visible');
    // this.map.setLayoutProperty(MapLayerId.NODE_ROUTE_ARROWS, 'visibility', 'visible');
    // this.map.setLayoutProperty(MapLayerId.NODE, 'visibility', 'visible');
    // this.map.setLayoutProperty(MapLayerId.NODE_NAME, 'visibility', 'visible');
  }

  private updateBackgroundVisibility(value: string): void {
    const layers = this.map.getStyle().layers;
    layers.forEach((layer) => {
      if (layer['source'] === 'openmaptiles' || layer.id == 'background') {
        this.map.setLayoutProperty(layer.id, 'visibility', value);
      }
    });
  }

  private filterRoutes(filter: FilterSpecification | null): void {
    const layerId = new RouteSourceIds('hiking').nodeRouteLayerId();
    this.map.setFilter(layerId, filter);
    // this.map.setFilter(MapLayerId.NODE_ROUTE_ARROWS, filter);
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
