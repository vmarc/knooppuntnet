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
      // style: 'https://tiles.openfreemap.org/styles/liberty',
      style: '/assets/liberty.json',
      center: [4.46839, 51.46774],
      zoom: 13,
    });
    map.on('styleimagemissing', (e) => {
      // Add a transparent image to prevent the warning
      map.addImage(e.id, {
        width: 1,
        height: 1,
        data: new Uint8Array([0, 0, 0, 0]),
      });
    });
    this.mapInstance = map;
    map.showTileBoundaries = true;
    map.on('load', () => {
      SourceRoutes.init(map);
    });
  }

  destroy(): void {
    if (this.mapInstance) {
      this.mapInstance.remove();
    }
  }
  xxx(): void {
    console.log('xxx called');
    //   this.mapInstance?.getSource('routes')?;
  }
}
