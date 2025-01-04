import { inject } from '@angular/core';
import { effect } from '@angular/core';
import { Injectable } from '@angular/core';
import { Bounds } from '@api/common';
import { LatLonImpl } from '@api/common';
import { Util } from '@app/components/shared';
import { OlUtil } from '@app/ol';
import { ZoomLevel } from '@app/ol/domain';
import { MapControls } from '@app/ol/layers';
import { State } from '@app/state';
import { FeatureLike } from 'ol/Feature';
import VectorTileLayer from 'ol/layer/VectorTile';
import Map from 'ol/Map';
import View from 'ol/View';
import { FocusElements } from './focus-elements';
import { Layers } from './layers/layers';
import { PoiService } from './poi/poi.service';
import { MapRoutePopupAction } from './popup/map-route-popup-handler';
import { MapRoutePopupInteraction } from './popup/map-route-popup-interaction';

@Injectable()
export class MapService {
  private readonly poiService = inject(PoiService);
  private readonly state = inject(State);
  private _map: Map;

  private readonly updateResolution = () => {
    this.state.map.updateViewZoom(this._map.getView().getZoom());
  };

  private readonly updateCenter = () => {
    this.state.map.updateCenter(this._map.getView().getCenter());
  };

  private readonly layers = new Layers(
    this.state,
    this.state.map.mapStyleOptions,
    this.state.map.poiStyleMap,
    this.state.map.poiActive
  );

  action: MapRoutePopupAction;

  constructor() {
    effect(() => {
      const state = this.state.map.routePopupState();
      if (this.action && state) {
        this.action(state.routes, state.coordinate);
      }
    });

    effect(() => {
      const styleOptions = this.state.map.mapStyleOptions();
      this.layers.routeLayerChanged();
      const options = [
        'zoom=' + styleOptions.zoom,
        'mode=' + styleOptions.mode,
        'international=' + styleOptions.scopeInternational,
        'national=' + styleOptions.scopeNational,
        'regional=' + styleOptions.scopeRegional,
        'local=' + styleOptions.scopeLocal,
        'nodeRoutes=' + styleOptions.scopeNodeRoutes,
        'route=' + styleOptions.selectedRoute,
      ];
      console.log(`mapStyleOptions ${options.join(', ')}`);
    });
    effect(() => {
      const xxx = this.state.map.poiActive();
      console.log('poiActive changed', xxx);
      this.layers.poiLayer.layer.changed();
    });
  }

  xxx(action: MapRoutePopupAction): void {
    this.action = action;
  }

  init(): void {
    const mapLayers = this.layers.all.map((mapLayer) => mapLayer.layer);
    this._map = new Map({
      target: 'main-map',
      layers: mapLayers,
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
    this._map.getView().setZoom(15);

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

  focusElements(bounds: Bounds, elements: FocusElements) {
    if (this._map !== null) {
      if (bounds) {
        this._map.getView().fit(Util.toExtent(bounds, 0.1));
      }
      this.state.map.updateFocusElements(elements);
    }
  }

  focusNode(latLon: LatLonImpl, nodeId: string) {
    if (this._map !== null) {
      const center = OlUtil.latLonToCoordinate(latLon);
      this._map.getView().setCenter(center);
      this.state.map.updateFocusElements({
        nodeIds: [nodeId],
        routeIds: [],
      });
    }
  }

  allFeatures(): FeatureLike[] {
    const features: FeatureLike[] = [];
    this.layers.all.forEach((mapLayer) => {
      if (mapLayer.layerType === 'route' && mapLayer.layer.getVisible()) {
        const vl = mapLayer.layer as VectorTileLayer;
        const extent = this._map.getView().getViewStateAndExtent().extent;
        features.push(...vl.getSource().getFeaturesInExtent(extent));
      }
    });
    return features;
  }
}
