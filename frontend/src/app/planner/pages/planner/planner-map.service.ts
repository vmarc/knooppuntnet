import { computed } from '@angular/core';
import { inject } from '@angular/core';
import { Injectable } from '@angular/core';
import { NetworkType } from '@api/custom';
import { PreferencesService } from '@app/core';
import { ZoomLevel } from '@app/ol/domain';
import { MapGeocoder } from '@app/ol/domain';
import { MapLayerState } from '@app/ol/domain';
import { MapControls } from '@app/ol/layers';
import { MapLayer } from '@app/ol/layers';
import { OpenlayersMapService } from '@app/ol/services';
import { MapZoomService } from '@app/ol/services';
import { PoiTileLayerService } from '@app/ol/services';
import { MapMode } from '@app/ol/services';
import { MainMapStyleParameters } from '@app/ol/style';
import { PoiService } from '@app/services';
import { Subscriptions } from '@app/util';
import Map from 'ol/Map';
import Overlay from 'ol/Overlay';
import View from 'ol/View';
import { SharedStateService } from '../../../shared/core/shared/shared-state.service';
import { PlannerInteraction } from '../../domain/interaction/planner-interaction';
import { PlannerMapLayerService } from './planner-map-layer.service';
import { PlannerState } from './planner-state';
import { PlannerStateService } from './planner-state.service';
import { PlannerService } from './planner.service';

@Injectable({
  providedIn: 'root',
})
export class PlannerMapService extends OpenlayersMapService {
  private readonly plannerService = inject(PlannerService);
  private readonly poiService = inject(PoiService);
  private readonly mapZoomService = inject(MapZoomService);
  private readonly preferencesService = inject(PreferencesService);
  private readonly sharedStateService = inject(SharedStateService);
  private readonly plannerStateService = inject(PlannerStateService);
  private readonly plannerMapLayerService = inject(PlannerMapLayerService);

  private overlay: Overlay;
  private readonly interaction = new PlannerInteraction(this.plannerService.engine);

  private networkType: NetworkType;
  private mapMode: MapMode;

  private parameters = computed(() => {
    const selectedRouteId = '';
    const selectedNodeId = '';
    const showProposed = this.preferencesService.showProposed();
    const surveyDateValues = this.sharedStateService.surveyDateValues();

    return new MainMapStyleParameters(
      this.plannerStateService.mapMode(),
      showProposed,
      surveyDateValues,
      selectedRouteId,
      selectedNodeId
    );
  });

  private subcriptions = new Subscriptions();

  // constructor() {
  //   super();
  //   effect(() => {
  //     this.plannerStateService.setLayerStates(this.layerStates());
  //   });
  // }

  init(state: PlannerState): void {
    const registry = this.plannerMapLayerService.registerLayers(
      state.networkType,
      state.urlLayerIds,
      this.parameters
    );
    this.plannerStateService.setLayerStates(registry.layerStates);
    this.register(registry);

    this.subcriptions.unsubscribe();

    this.overlay = this.buildOverlay();

    this.initMap(
      new Map({
        target: this.mapId,
        layers: this.layers,
        overlays: [this.overlay],
        controls: MapControls.build(),
        view: new View({
          minZoom: ZoomLevel.minZoom,
          maxZoom: ZoomLevel.vectorTileMaxOverZoom,
        }),
      })
    );

    this.map.getView().setZoom(state.position.zoom);
    this.map.getView().setCenter([state.position.x, state.position.y]);

    this.plannerService.init(this.map);
    this.interaction.addToMap(this.map);

    const view = this.map.getView();

    this.poiService.updateZoomLevel(view.getZoom()); // TODO can do better?
    this.mapZoomService.install(view); // TODO eliminate

    MapGeocoder.install(this.map);

    this.finalizeSetup();

    // TODO SIGNAL
    // this.plannerStateService.setMapPosition(this.mapPosition);
    // this.plannerStateService.setLayerStates(this.layerStates);
    //
    // this.subcriptions.add(
    //   this.mapPosition$
    //     .pipe(skip(1))
    //     .subscribe((mapPosition) => this.plannerStateService.setMapPosition(mapPosition))
    // );
  }

  setNetworkType(networkType: NetworkType): void {
    this.networkType = networkType;
  }

  setMapMode(mapMode: MapMode): void {
    this.mapMode = mapMode;
    this.updateLayerVisibility();
  }

  override destroy() {
    // this.networkVectorLayerStyle.destroy(); can we really do this? consider the lifecycle of this service...
    this.subcriptions.unsubscribe();
    super.destroy();
  }

  networkTypeChanged(networkType: NetworkType) {
    let changed = false;
    const newLayerStates = this.layerStates().map((layerState) => {
      let enabled = layerState.enabled;
      const correspondingMapLayer = this.mapLayers.find(
        (mapLayer) => mapLayer.id === layerState.id
      );
      if (correspondingMapLayer && correspondingMapLayer.networkType) {
        enabled = correspondingMapLayer.networkType === networkType;
      }
      if (enabled !== layerState.enabled) {
        changed = true;
        const visible = layerState.id === networkType;
        return { ...layerState, visible, enabled };
      }
      return layerState;
    });
    if (changed) {
      this.updateLayerStates(newLayerStates);
      this.updateLayerVisibility();
    }
  }

  networkTypeOrMapModeChanged(networkType: NetworkType, mapMode: MapMode) {
    this.networkType = networkType;
    this.mapMode = mapMode;
    this.updateLayerVisibility();
  }

  protected override layerVisible(mapLayer: MapLayer): boolean {
    if (!!mapLayer.networkType && mapLayer.networkType !== this.networkType) {
      return false;
    }
    if (!!mapLayer.mapMode && mapLayer.mapMode !== this.mapMode) {
      return false;
    }
    return super.layerVisible(mapLayer);
  }

  plannerUpdatePoiLayerVisibility(newLayerStates: MapLayerState[]): void {
    this.updateLayerStates(newLayerStates);
    this.mapLayers.forEach((mapLayer) => {
      if (mapLayer.name === PoiTileLayerService.poiLayerId) {
        const mapLayerState = this.layerStates().find(
          (layerState) => layerState.id === mapLayer.id
        );
        if (mapLayerState) {
          mapLayer.layer.setVisible(mapLayerState.visible);
        }
      }
    });
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
