import { Injectable } from '@angular/core';
import { Map as MaplibreMap } from 'maplibre-gl';
import { RouteSource } from './sources/route-source';
import { FilterSpecification } from '@maplibre/maplibre-gl-style-spec';

@Injectable({
  providedIn: 'root',
})
export class NewMapService {
  private map: MaplibreMap | null = null;

  init(): void {
    const mapLibreMap = new MaplibreMap({
      container: 'map',
      // style: 'https://tiles.openfreemap.org/styles/liberty',
      style: '/assets/liberty.json',
      center: [4.46839, 51.46774],
      zoom: 13,
    });
    this.map = mapLibreMap;
    this.preventImageMissingWarning(mapLibreMap);
    mapLibreMap.showTileBoundaries = true;
    mapLibreMap.on('load', () => {
      RouteSource.init(mapLibreMap, 'hiking');
    });
  }

  selectRoutes(routeIds: string[]): void {
    const filter: FilterSpecification = ['in', ['get', 'routeId'], ['literal', routeIds]];
    this.filterRoutes(filter);
  }

  resetRouteSelection(): void {
    this.filterRoutes(null);
  }

  private filterRoutes(filter: FilterSpecification | null): void {
    if (this.map) {
      this.map.setFilter('node-route', filter);
      this.map.setFilter('node-route-arrows', filter);
    }
  }

  destroy(): void {
    if (this.map) {
      this.map.remove();
    }
  }

  initRouteType(routeType: string): void {
    if (this.map) {
      RouteSource.remove(this.map);
      RouteSource.init(this.map, routeType);
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
