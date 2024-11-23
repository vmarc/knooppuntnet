import { inject } from '@angular/core';
import { effect } from '@angular/core';
import { Injectable } from '@angular/core';
import { LatLonImpl } from '@api/common';
import { OlUtil } from '@app/ol';
import { ZoomLevel } from '@app/ol/domain';
import { MapControls } from '@app/ol/layers';
import { State } from '@app/state';
import Map from 'ol/Map';
import View from 'ol/View';
import { Layers } from './layers/layers';
import { MapRoutePopupAction } from './popup/map-route-popup-handler';
import { MapRoutePopupInteraction } from './popup/map-route-popup-interaction';

@Injectable()
export class MapService {
  private readonly state = inject(State);
  private _map: Map;

  private readonly updateResolution = () => {
    this.state.map.updateViewZoom(this._map.getView().getZoom());
  };

  private readonly updateCenter = () => {
    this.state.map.updateCenter(this._map.getView().getCenter());
  };

  private readonly layers = new Layers(this.state.map.zoom);

  action: MapRoutePopupAction;

  constructor() {
    effect(
      () => {
        const state = this.state.map.routePopupState();
        if (this.action && state) {
          this.action(state.routes, state.coordinate);
        }
      },
      {
        allowSignalWrites: true,
      }
    );

    effect(
      () => {
        const selectedRoute = this.state.map.selectedRoute();
        this.layers.routeLayer.changed();
      },
      {
        allowSignalWrites: true,
      }
    );
  }

  xxx(action: MapRoutePopupAction): void {
    this.action = action;
  }

  init(): void {
    this._map = new Map({
      target: 'main-map',
      layers: [this.layers.osmLayer, this.layers.routeLayer, this.layers.grid256Layer],
      controls: MapControls.build(),
      view: new View({
        minZoom: ZoomLevel.newMinZoom,
        maxZoom: ZoomLevel.vectorTileMaxOverZoom, //ZoomLevel.maxZoom,
        zoom: 6,
      }),
    });

    const essen: LatLonImpl = { latitude: '51.46774', longitude: '4.46839' };
    const center = OlUtil.latLonToCoordinate(essen);
    this._map.getView().setCenter(center);
    // this._map.getView().setZoom(14);

    const view = this._map.getView();
    view.on('change:resolution', this.updateResolution);
    view.on('change:center', this.updateCenter);
    this.updateResolution();
    this.updateCenter();

    const interaction = new MapRoutePopupInteraction(this.state);

    this._map.addInteraction(interaction);
  }

  destroy(): void {
    if (this._map) {
      this._map.getView().un('change:resolution', this.updateResolution);
      this._map.getView().un('change:center', this.updateCenter);
      this._map.dispose();
      this._map.setTarget(null);
    }
  }
}
