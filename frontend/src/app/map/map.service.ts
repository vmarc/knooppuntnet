import { Injectable } from '@angular/core';
import { LatLonImpl } from '@api/common';
import { OlUtil } from '@app/ol';
import { ZoomLevel } from '@app/ol/domain';
import { MapControls } from '@app/ol/layers';
import TileLayer from 'ol/layer/Tile';
import Map from 'ol/Map';
import OSM from 'ol/source/OSM';
import View from 'ol/View';

@Injectable()
export class MapService {
  private _map: Map;

  init(): void {
    const osmLayer = new TileLayer({
      source: new OSM({
        url: 'https://tile.openstreetmap.org/{z}/{x}/{y}.png',
      }),
    });

    this._map = new Map({
      target: 'main-map',
      layers: [osmLayer],
      controls: MapControls.build(),
      view: new View({
        minZoom: ZoomLevel.minZoom,
        maxZoom: ZoomLevel.maxZoom,
        zoom: 18,
      }),
    });

    const essen: LatLonImpl = { latitude: '51.46774', longitude: '4.46839' };
    const center = OlUtil.latLonToCoordinate(essen);
    this._map.getView().setCenter(center);
  }

  destroy(): void {
    if (this._map) {
      // this._map.getView().un('change:resolution', this.updatePositionHandler);
      // this._map.getView().un('change:center', this.updatePositionHandler);
      this._map.dispose();
      this._map.setTarget(null);
    }
  }
}
