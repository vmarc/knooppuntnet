import { Injectable } from '@angular/core';
import { Coordinate } from 'ol/coordinate';
import { Extent } from 'ol/extent';
import { fromLonLat } from 'ol/proj';
import View from 'ol/View';
import { BrowserStorageService } from '../../../services/browser-storage.service';
import { MapPosition } from '../domain/map-position';
import { MapPositionService } from './map-position.service';

@Injectable()
export class MainMapPositionService {
  private mapPositionKey = 'map-position';

  private view: View;

  constructor(
    private storage: BrowserStorageService,
    private mapPositionService: MapPositionService
  ) {
    mapPositionService.mapPosition$.subscribe((mapPosition) => {
      if (mapPosition) {
        this.storage.set(this.mapPositionKey, JSON.stringify(mapPosition));
      }
    });
  }

  install(view: View, mapPositionFromUrl: MapPosition): void {
    this.view = view;
    this.mapPositionService.install(view);
    if (mapPositionFromUrl) {
      this.gotoInitialMapPosition(mapPositionFromUrl);
    } else {
      const mapPositionString = this.storage.get(this.mapPositionKey);
      if (mapPositionString == null) {
        this.gotoDefaultInitialPosition();
      } else {
        this.gotoInitialPosition(mapPositionString);
      }
    }
  }

  private gotoDefaultInitialPosition(): void {
    const a: Coordinate = fromLonLat([2.24, 50.16]);
    const b: Coordinate = fromLonLat([10.56, 54.09]);
    const extent: Extent = [a[0], a[1], b[0], b[1]];
    this.view.fit(extent);
  }

  private gotoInitialPosition(mapPositionString: string): void {
    const mapPosition = MapPosition.fromJSON(JSON.parse(mapPositionString));
    this.gotoInitialMapPosition(mapPosition);
  }

  private gotoInitialMapPosition(mapPosition: MapPosition): void {
    this.view.setZoom(mapPosition.zoom);
    this.view.setRotation(mapPosition.rotation);
    const center: Coordinate = [mapPosition.x, mapPosition.y];
    this.view.setCenter(center);
  }
}
