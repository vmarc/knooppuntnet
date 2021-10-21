import { Injectable } from '@angular/core';
import { Bounds } from '@api/common/bounds';
import { Coordinate } from 'ol/coordinate';
import View from 'ol/View';
import { BrowserStorageService } from '../../../services/browser-storage.service';
import { Util } from '../../shared/util';
import { NetworkMapPosition } from '../domain/network-map-position';

@Injectable()
export class NetworkMapPositionService {
  private networkMapPositionKey = 'network-map-position';

  private view: View;

  constructor(private storage: BrowserStorageService) {}

  install(view: View, networkId: number, bounds: Bounds): void {
    this.view = view;
    const mapPositionString = this.storage.get(this.networkMapPositionKey);
    if (mapPositionString == null) {
      view.fit(Util.toExtent(bounds, 0.1));
    } else {
      const mapPosition: NetworkMapPosition = JSON.parse(mapPositionString);
      if (networkId === mapPosition.networkId) {
        this.gotoLastKnownPosition(mapPosition);
      } else {
        view.fit(Util.toExtent(bounds, 0.1));
      }
    }
    this.updateUponPositionChange(networkId);
  }

  private gotoLastKnownPosition(mapPosition: NetworkMapPosition): void {
    this.view.setZoom(mapPosition.zoom);
    this.view.setRotation(mapPosition.rotation);
    const center: Coordinate = [mapPosition.x, mapPosition.y];
    this.view.setCenter(center);
  }

  private updateUponPositionChange(networkId: number): void {
    this.view.on('change:resolution', () => this.updateLocalStorage(networkId));
    this.view.on('change:center', (e) => this.updateLocalStorage(networkId));
    this.view.on('change:rotation', (e) => this.updateLocalStorage(networkId));
  }

  private updateLocalStorage(networkId: number): void {
    const center: Coordinate = this.view.getCenter();
    const zoom = this.view.getZoom();
    const rotation = this.view.getRotation();
    const networkMapPosition: NetworkMapPosition = {
      networkId: networkId,
      zoom: zoom,
      x: center[0],
      y: center[1],
      rotation: rotation,
    };
    this.storage.set(
      this.networkMapPositionKey,
      JSON.stringify(networkMapPosition)
    );
  }
}
