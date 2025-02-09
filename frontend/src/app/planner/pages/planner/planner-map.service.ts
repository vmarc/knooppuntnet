import { computed } from '@angular/core';
import { inject } from '@angular/core';
import { Injectable } from '@angular/core';
import { MapZoomService } from '@app/ol/services/map-zoom.service';
import { MainMapStyleParameters } from '@app/ol/style/main-map-style-parameters';
import { OldPoiService } from '@app/shared/services/old-poi.service';
import { State } from '@app/state/state';
import { Subscriptions } from '@app/util/subscriptions';
import Map from 'ol/Map';
import Overlay from 'ol/Overlay';
import { SharedStateService } from '@app/shared/core/shared/shared-state.service';
import { PlannerInteraction } from '../../domain/interaction/planner-interaction';
import { PlannerService } from './planner.service';

@Injectable({
  providedIn: 'root',
})
export class PlannerMapService {
  private readonly state = inject(State);
  private readonly plannerService = inject(PlannerService);
  private readonly poiService = inject(OldPoiService);
  private readonly mapZoomService = inject(MapZoomService);
  private readonly sharedStateService = inject(SharedStateService);

  readonly interaction = new PlannerInteraction(this.plannerService.engine).interaction;

  private parameters = computed(() => {
    const selectedRouteId = '';
    const selectedNodeId = '';
    const showProposed = this.state.preferences.showProposed();
    const surveyDateValues = this.sharedStateService.surveyDateValues();

    return new MainMapStyleParameters(
      this.state.planner.mapMode(),
      showProposed,
      surveyDateValues,
      selectedRouteId,
      selectedNodeId
    );
  });

  private subcriptions = new Subscriptions();

  constructor() {
    // super();
    // effect(() => {
    //   const routeType = this.state.page.routeType();
    //   this.routeTypeChanged(routeType);
    // });
  }

  init(map: Map): void {
    // const registry = this.plannerMapLayerService.registerLayers(
    //   this.state.page.routeType(),
    //   this.state.planner.urlLayerIds(),
    //   this.parameters
    // );
    // this.state.planner.updateLayerStates(registry.layerStates);
    // this.register(registry);
    //
    // this.subcriptions.unsubscribe();
    //
    // this.overlay = this.buildOverlay();
    //
    // this.initMap(
    //   new Map({
    //     target: this.mapId,
    //     layers: this.layers,
    //     overlays: [this.overlay],
    //     controls: MapControls.build(),
    //     view: new View({
    //       minZoom: ZoomLevel.minZoom,
    //       maxZoom: ZoomLevel.vectorTileMaxOverZoom,
    //     }),
    //   })
    // );
    //
    // const position = this.state.planner.position();
    // this.map.getView().setZoom(position.zoom);
    // this.map.getView().setCenter([position.x, position.y]);
    //
    this.plannerService.init(map);

    const view = map.getView();

    this.poiService.updateZoomLevel(view.getZoom()); // TODO can do better?
    this.mapZoomService.install(view); // TODO eliminate

    // MapGeocoder.install(this.map);
    //
    // this.finalizeSetup(true);
  }

  destroy() {
    // this.networkVectorLayerStyle.destroy(); can we really do this? consider the lifecycle of this service...
    this.subcriptions.unsubscribe();
    // super.destroy();
  }

  // private routeTypeChanged(routeType: RouteType) {
  //   let changed = false;
  //   const newLayerStates = this.layerStates().map((layerState) => {
  //     let enabled = layerState.enabled;
  //     const correspondingMapLayer = this.mapLayers.find(
  //       (mapLayer) => mapLayer.id === layerState.id
  //     );
  //     if (correspondingMapLayer && correspondingMapLayer.routeType) {
  //       enabled = correspondingMapLayer.routeType === routeType;
  //     }
  //     if (enabled !== layerState.enabled) {
  //       changed = true;
  //       const visible = layerState.id === routeType;
  //       return { ...layerState, visible, enabled };
  //     }
  //     return layerState;
  //   });
  //   if (changed) {
  //     this.updateLayerStates(newLayerStates);
  //     this.updateLayerVisibility();
  //   }
  // }

  // protected override layerVisible(mapLayer: OldMapLayer): boolean {
  //   if (!!mapLayer.routeType && mapLayer.routeType !== this.state.page.routeType()) {
  //     return false;
  //   }
  //   if (!!mapLayer.mapMode && mapLayer.mapMode !== this.state.planner.mapMode()) {
  //     return false;
  //   }
  //   return super.layerVisible(mapLayer);
  // }

  // plannerUpdatePoiLayerVisibility(newLayerStates: MapLayerState[]): void {
  //   this.updateLayerStates(newLayerStates);
  //   this.mapLayers.forEach((mapLayer) => {
  //     if (mapLayer.name === OldPoiTileLayerService.poiLayerId) {
  //       const mapLayerState = this.layerStates().find(
  //         (layerState) => layerState.id === mapLayer.id
  //       );
  //       if (mapLayerState) {
  //         mapLayer.layer.setVisible(mapLayerState.visible);
  //       }
  //     }
  //   });
  // }

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
