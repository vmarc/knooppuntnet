import { signal } from '@angular/core';
import { Injectable } from '@angular/core';
import { View } from 'ol';

@Injectable({
  providedIn: 'root',
})
export class MapZoomService {
  private previousZoomLevel = 0;
  private readonly _zoomLevel = signal<number | null>(null);
  readonly zoomLevel = this._zoomLevel.asReadonly();

  install(view: View): void {
    this.updateZoomLevel(view.getZoom());
    view.on('change:resolution', () => this.updateZoomLevel(view.getZoom()));
  }

  private updateZoomLevel(zoomLevel: number): void {
    const roundedZoomLevel = Math.round(zoomLevel);
    if (roundedZoomLevel !== this.previousZoomLevel) {
      this.previousZoomLevel = roundedZoomLevel;
      this._zoomLevel.set(roundedZoomLevel);
    }
  }
}
