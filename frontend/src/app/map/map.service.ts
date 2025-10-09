import { HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { effect } from '@angular/core';
import { Injectable } from '@angular/core';
import { Bounds } from '@api/common/bounds';
import { LatLonImpl } from '@api/common/lat-lon-impl';
import { PlanParams } from '@api/common/planner/plan-params';
import { OlUtil } from '@app/ol/ol-util';
import { NoRouteDialogComponent } from '@app/ol/components/no-route-dialog.component';
import { LegNotFoundDialogComponent } from '@app/ol/components/leg-not-found-dialog';
import { LegHttpErrorDialogComponent } from '@app/ol/components/leg-http-error.dialog';
import { ZoomLevel } from '@app/ol/domain/zoom-level';
import { MapControls } from '@app/ol/layers/map-controls';
import { MapMode } from '@app/map/domain/map-mode';
import { ApiService } from '@app/shared/services/api.service';
import { Util } from '@app/shared/components/util';
import { State } from '@app/state/state';
import { Subscriptions } from '@app/util/subscriptions';
import { NzModalService } from 'ng-zorro-antd/modal';
import { Coordinate } from 'ol/coordinate';
import { FeatureLike } from 'ol/Feature';
import VectorTileLayer from 'ol/layer/VectorTile';
import Map from 'ol/Map';
import View from 'ol/View';
import { PlannerCommandAddPlan } from '../planner/domain/commands/planner-command-add-plan';
import { PlanBuilder } from '../planner/domain/plan/plan-builder';
import { PlanUtil } from '../planner/domain/plan/plan-util';
import { PlannerMapService } from '../planner/pages/planner/planner-map.service';
import { PlannerStateService } from '../planner/pages/planner/planner-state.service';
import { PlannerService } from '../planner/pages/planner/planner.service';
import { SharedStateService } from '../shared/core/shared/shared-state.service';
import { RouterService } from '@app/shared/services/router.service';
import { FocusElements } from '../state/focus-elements';
import { Layers } from './layers/layers';
import { MapInteractionsService } from './map-interactions.service';
import { MapRoutePopupAction } from './popup/map-route-popup-handler';

@Injectable()
export class MapService {
  private readonly state = inject(State);
  private _map: Map;

  private readonly mapInteractionsService = inject(MapInteractionsService);
  private readonly plannerStateService = inject(PlannerStateService);
  private readonly plannerService = inject(PlannerService);
  private readonly plannerMapService = inject(PlannerMapService);
  private readonly modalService = inject(NzModalService);
  private readonly apiService = inject(ApiService);
  private readonly sharedStateService = inject(SharedStateService);
  private readonly routerService = inject(RouterService);

  private readonly subscriptions = new Subscriptions();

  private readonly updateResolution = () => {
    this.state.map.updateViewZoom(this._map.getView().getZoom());
  };

  private readonly updateCenter = () => {
    this.state.map.updateCenter(this._map.getView().getCenter());
  };

  private readonly layers = new Layers(
    this.state,
    this.state.map.mapStyleOptions,
    this.state.map.monitorMapState,
    this.state.map.poiStyleMap,
    this.state.map.poiActive
  );

  action: MapRoutePopupAction;

  constructor() {
    this.plannerStateService.onInit();
    this.sharedStateService.loadSurveyDateValues();
    effect(() => {
      const routeType = this.state.page.routeType();
      this.plannerService.context.setRouteType(routeType);
    });
    effect(() => {
      const error = this.plannerService.context.error();
      if (error) {
        if (error instanceof HttpErrorResponse) {
          this.modalService.create({
            nzContent: LegHttpErrorDialogComponent,
            nzFooter: null,
          });
        } else if ('leg-not-found' === error.message) {
          this.modalService.create({
            nzContent: LegNotFoundDialogComponent,
            nzFooter: null,
          });
        }
      }
    });
    effect(() => {
      const state = this.state.map.routePopupState();
      if (this.action && state) {
        this.action(state.routes, state.coordinate);
      }
    });

    effect(() => {
      this.state.map.mapStyleOptions();
      this.layers.routeLayerChanged();
    });

    effect(() => {
      this.state.map.monitorMapState();
      this.layers.monitorLayerChanged();
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
    const routeType = this.state.page.routeType();
    const planString = this.routerService.queryParam('plan');
    if (planString) {
      const planParams: PlanParams = {
        routeType,
        planString,
      };
      this.apiService.plan(planParams).subscribe((response) => {
        const plan = PlanBuilder.build(response.result, planString);
        const command = new PlannerCommandAddPlan(plan);
        this.plannerService.context.execute(command);
        if (this._map) {
          this.zoomInToRoute();
        }
      });
    }

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

    // TODO redesign - use different way to determine initial center of the map
    const essen: LatLonImpl = { latitude: '51.46774', longitude: '4.46839' };
    const center = OlUtil.latLonToCoordinate(essen);
    this._map.getView().setCenter(center);
    this._map.getView().setZoom(15);

    const view = this._map.getView();
    view.on('change:resolution', this.updateResolution);
    view.on('change:center', this.updateCenter);
    this.updateResolution();
    this.updateCenter();

    this.mapInteractionsService.init(this._map);
    this.plannerMapService.init(this._map);
  }

  destroy(): void {
    if (this._map) {
      this._map.getView().un('change:resolution', this.updateResolution);
      this._map.getView().un('change:center', this.updateCenter);
      this._map.dispose();
      this._map.setTarget(null);
    }
    this.subscriptions.unsubscribe();
    this.plannerService.context.destroy();
    this.plannerMapService.destroy();
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

  setMapMode(mapMode: MapMode): void {
    this.state.map.updateMode(mapMode);
    this.state.planner.updateMapMode(mapMode);
    // this.plannerMapService.updateLayerVisibility();
  }

  mouseleave() {
    this.plannerService.engine.handleMouseLeave();
  }

  zoomInToRoute(): void {
    if (this.plannerService.context.plan().legs.isEmpty()) {
      this.modalService.create({
        nzContent: NoRouteDialogComponent,
        nzFooter: null,
      });
    } else {
      const bounds = PlanUtil.planBounds(this.plannerService.context.plan());
      if (bounds !== null) {
        const extent = Util.toExtent(bounds, 0.1);
        this._map.getView().fit(extent);
      }
    }
  }

  fitBounds(bounds: Bounds): void {
    if (bounds) {
      const extent = Util.toExtent(bounds, 0.1);
      this._map.getView().fit(extent);
    }
  }

  geolocation(coordinate: Coordinate): void {
    this._map.getView().setCenter(coordinate);
    let zoomLevel = 15;
    if ('cycling' === this.state.page.routeType()) {
      zoomLevel = 13;
    }
    this._map.getView().setZoom(zoomLevel);
  }
}
