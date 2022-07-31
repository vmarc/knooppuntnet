import { Injectable } from '@angular/core';
import { Bounds } from '@api/common/bounds';
import { Coordinate } from 'ol/coordinate';
import View from 'ol/View';
import { BrowserStorageService } from '../../../services/browser-storage.service';
import { Util } from '../../shared/util';
import { NetworkMapPosition } from '../domain/network-map-position';
import { MapPositionService } from './map-position.service';

@Injectable()
export class NetworkMapPositionService {
  private networkMapPositionKey = 'network-map-position';

  private view: View;
  private networkId: number;

  constructor(
    private storage: BrowserStorageService,
    private mapPositionService: MapPositionService
  ) {
    mapPositionService.mapPosition$.subscribe((mapPosition) => {
      if (mapPosition) {
        const networkMapPosition: NetworkMapPosition = {
          networkId: this.networkId,
          zoom: mapPosition.zoom,
          x: mapPosition.x,
          y: mapPosition.y,
          rotation: mapPosition.rotation,
        };
        this.storage.set(
          this.networkMapPositionKey,
          JSON.stringify(networkMapPosition)
        );
      }
    });
  }

  install(
    view: View,
    networkId: number,
    bounds: Bounds,
    mapPositionFromUrl: NetworkMapPosition
  ): void {
    this.view = view;
    this.networkId = networkId;
    this.mapPositionService.install(view);
    if (mapPositionFromUrl) {
      this.gotoLastKnownPosition(mapPositionFromUrl);
    } else {
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
    }
  }

  private gotoLastKnownPosition(mapPosition: NetworkMapPosition): void {
    this.view.setZoom(mapPosition.zoom);
    this.view.setRotation(mapPosition.rotation);
    const center: Coordinate = [mapPosition.x, mapPosition.y];
    this.view.setCenter(center);
  }
}
