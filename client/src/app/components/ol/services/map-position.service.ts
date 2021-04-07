import { Injectable } from '@angular/core';
import { Coordinate } from 'ol/coordinate';
import { Extent } from 'ol/extent';
import { fromLonLat } from 'ol/proj';
import View from 'ol/View';
import { BrowserStorageService } from '../../../services/browser-storage.service';
import { MapPosition } from '../domain/map-position';

@Injectable()
export class MapPositionService {
  private mapPositionKey = 'map-position';

  private view: View;

  constructor(private storage: BrowserStorageService) {}

  install(view: View): void {
    this.view = view;
    const mapPositionString = this.storage.get(this.mapPositionKey);
    if (mapPositionString == null) {
      this.gotoDefaultInitialPosition();
    } else {
      this.gotoInitialPosition(mapPositionString);
    }
    this.updateUponPositionChange();
  }

  private gotoDefaultInitialPosition(): void {
    const a: Coordinate = fromLonLat([2.24, 50.16]);
    const b: Coordinate = fromLonLat([10.56, 54.09]);
    const extent: Extent = [a[0], a[1], b[0], b[1]];
    this.view.fit(extent);
  }

  private gotoInitialPosition(mapPositionString: string): void {
    const mapPosition = MapPosition.fromJSON(JSON.parse(mapPositionString));
    this.view.setZoom(mapPosition.zoom);
    this.view.setRotation(mapPosition.rotation);
    const center: Coordinate = [mapPosition.x, mapPosition.y];
    this.view.setCenter(center);
  }

  private updateUponPositionChange(): void {
    this.view.on('change:resolution', () => this.update());
    this.view.on('change:center', (e) => this.update());
    this.view.on('change:rotation', (e) => this.update());
  }

  private update(): void {
    const center: Coordinate = this.view.getCenter();
    const zoom = this.view.getZoom();
    const rotation = this.view.getRotation();
    const mapPosition = new MapPosition(zoom, center[0], center[1], rotation);
    this.storage.set(this.mapPositionKey, JSON.stringify(mapPosition));
  }
}
