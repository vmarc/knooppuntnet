import { Injectable, inject } from '@angular/core';
import { Map as MaplibreMap } from 'maplibre-gl';
import { RouteSource } from './sources/route-source';
import { FilterSpecification } from '@maplibre/maplibre-gl-style-spec';
import { MapLayerId } from './constants/map-layer-id';
import { State } from '@app/state/state';

@Injectable()
export class MapService {
  private readonly state = inject(State);
  private map: MaplibreMap | null = null;

  init(): void {
    const mapLibreMap = new MaplibreMap({
      container: 'map',
      style: '/assets/liberty.json',
      center: [4.46839, 51.46774],
      zoom: 13,
    });
    this.map = mapLibreMap;
    this.preventImageMissingWarning(mapLibreMap);

    mapLibreMap.loadImage('/assets/arrow.png').then((response) => {
      mapLibreMap.addImage('node-route-arrow', response.data);
    });

    mapLibreMap.on('load', () => {
      RouteSource.init(mapLibreMap, this.state.preferences.routeType());
      // this.hideOsmBackground();
    });
  }

  hideOsmBackground(): void {
    this.updateOsmBackgroundVisibility('none');
  }

  showOsmBackground(): void {
    this.updateOsmBackgroundVisibility('visible');
  }

  private updateOsmBackgroundVisibility(value: string): void {
    const layers = this.map.getStyle().layers;
    layers.forEach((layer) => {
      if (layer['source'] === 'openmaptiles' || layer.id == 'background') {
        this.map.setLayoutProperty(layer.id, 'visibility', value);
      }
    });
  }

  selectRoutes(routeIds: string[]): void {
    const filter: FilterSpecification = ['in', ['get', 'routeId'], ['literal', routeIds]];
    this.filterRoutes(filter);
  }

  resetRouteSelection(): void {
    this.filterRoutes(null);
  }

  destroy(): void {
    if (this.map) {
      this.map.remove();
    }
  }

  initRouteType(routeType: string): void {
    if (this.map) {
      if (this.map.loaded()) {
        RouteSource.remove(this.map);
        RouteSource.init(this.map, routeType);
      } else {
        this.map.on('load', () => {
          RouteSource.remove(this.map);
          RouteSource.init(this.map, routeType);
        });
      }
    }
  }

  hideRouteLayer(): void {
    if (this.map) {
      this.map.setLayoutProperty(MapLayerId.ROUTE, 'visibility', 'none');
    }
  }

  showRouteLayer(): void {
    if (this.map) {
      this.map.setLayoutProperty(MapLayerId.ROUTE, 'visibility', 'visible');
    }
  }

  hideNodeRouteLayer(): void {
    if (this.map) {
      this.map.setLayoutProperty(MapLayerId.NODE_ROUTE, 'visibility', 'none');
      this.map.setLayoutProperty(MapLayerId.NODE_ROUTE_ARROWS, 'visibility', 'none');
      this.map.setLayoutProperty(MapLayerId.NODE, 'visibility', 'none');
      this.map.setLayoutProperty(MapLayerId.NODE_NAME, 'visibility', 'none');
    }
  }

  showNodeRouteLayer(): void {
    if (this.map) {
      this.map.setLayoutProperty(MapLayerId.NODE_ROUTE, 'visibility', 'visible');
      this.map.setLayoutProperty(MapLayerId.NODE_ROUTE_ARROWS, 'visibility', 'visible');
      this.map.setLayoutProperty(MapLayerId.NODE, 'visibility', 'visible');
      this.map.setLayoutProperty(MapLayerId.NODE_NAME, 'visibility', 'visible');
    }
  }

  private filterRoutes(filter: FilterSpecification | null): void {
    if (this.map) {
      this.map.setFilter('node-route', filter);
      this.map.setFilter('node-route-arrows', filter);
    }
  }
  private preventImageMissingWarning(map: MaplibreMap): void {
    map.on('styleimagemissing', (e) => {
      // Add a transparent image to prevent the warning
      map.addImage(e.id, {
        width: 1,
        height: 1,
        data: new Uint8Array([0, 0, 0, 0]),
      });
    });
  }
}
