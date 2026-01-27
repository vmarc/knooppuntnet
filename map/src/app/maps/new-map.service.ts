import { Injectable } from '@angular/core';
import { Map as MaplibreMap } from 'maplibre-gl';
import { SourceRoutes } from './sources/source-routes';

@Injectable({
  providedIn: 'root',
})
export class NewMapService {
  private mapInstance: MaplibreMap | null = null;

  init(): void {
    const map = new MaplibreMap({
      container: 'map',
      style: 'https://tiles.openfreemap.org/styles/liberty',
      center: [4.46839, 51.46774],
      zoom: 13,
    });

    this.mapInstance = map;
    map.showTileBoundaries = true;
    map.on('load', () => {
      //SourceOsmRaster.init(map);
      //SourceOpenFreeMap.init(map);
      SourceRoutes.init(map);

      console.log('map loaded', map);
    });
  }

  destroy(): void {
    if (this.mapInstance) {
      this.mapInstance.remove();
    }
  }
}
