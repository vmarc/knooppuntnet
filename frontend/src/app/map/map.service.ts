import { effect } from '@angular/core';
import { computed } from '@angular/core';
import { signal } from '@angular/core';
import { Injectable } from '@angular/core';
import { LatLonImpl } from '@api/common';
import { OlUtil } from '@app/ol';
import { ZoomLevel } from '@app/ol/domain';
import { MapControls } from '@app/ol/layers';
import { Coordinate } from 'ol/coordinate';
import Map from 'ol/Map';
import Overlay from 'ol/Overlay';
import View from 'ol/View';
import { HooverFeature } from './hoover-feature';
import { HooverState } from './hoover-state';
import { Layers } from './layers/layers';
import { RouteHooverInteraction } from './route-hoover-interaction';

@Injectable()
export class MapService {
  private _map: Map;

  private readonly _viewZoom = signal<number>(0);
  readonly zoom = computed(() => Math.floor(this._viewZoom()));

  private readonly _center = signal<Coordinate | null>(null);
  readonly center = this._center.asReadonly();

  private readonly _mode = signal<string>('explore');

  private readonly _hooverState = signal<HooverState | null>(null);
  readonly hooverState = this._hooverState.asReadonly();

  private readonly updateResolution = () => {
    this._viewZoom.set(this._map.getView().getZoom());
  };

  private readonly updateCenter = () => {
    this._center.set(this._map.getView().getCenter());
  };

  private readonly layers = new Layers(this.zoom);

  private selectedRoute = signal<number | null>(null);
  private overlay: Overlay;

  constructor() {
    effect(
      () => {
        const state = this.hooverState();
        if (this.overlay) {
          this.overlay.setOffset([0, -10]);
          if (state.routes.length > 0) {
            this.overlay.setPosition(state.coordinate);
          } else {
            this.overlay.setPosition(null);
          }
        }
      },
      {
        allowSignalWrites: true,
      }
    );
  }

  init(): void {
    this.overlay = this.buildOverlay();

    this._map = new Map({
      target: 'main-map',
      layers: [this.layers.osmLayer, this.layers.routeLayer, this.layers.grid256Layer],
      controls: MapControls.build(),
      overlays: [this.overlay],
      view: new View({
        minZoom: 6, //ZoomLevel.minZoom,
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
    this.overlay = this._map.getOverlayById('popup');
    console.log('overlay', this.overlay);

    const interaction = new RouteHooverInteraction(
      this._mode,
      this._hooverState,
      (hooverRoutes: Array<HooverFeature>, coordinate: Coordinate) => {
        console.log(`ACTION`, hooverRoutes, coordinate);
      }
    );

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

  selectRoute(routeId: number) {
    this.selectedRoute.set(routeId);
    this.layers.routeLayer.changed();
  }

  private buildOverlay(): Overlay {
    return new Overlay({
      id: 'popup',
      element: document.getElementById('popup'),
      autoPan: {
        animation: {
          duration: 250,
        },
      },
    });
  }
}
