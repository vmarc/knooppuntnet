import { Injectable } from '@angular/core';
import { Params } from '@angular/router';
import { NetworkType } from '@api/custom';
import { ZoomLevel } from '@app/components/ol/domain';
import { MapGeocoder } from '@app/components/ol/domain';
import { MapLayerState } from '@app/components/ol/domain';
import { MapLayerRegistry } from '@app/components/ol/layers';
import { BackgroundLayer } from '@app/components/ol/layers';
import { OsmLayer } from '@app/components/ol/layers';
import { MapControls } from '@app/components/ol/layers';
import { TileDebug256Layer } from '@app/components/ol/layers';
import { TileDebug512Layer } from '@app/components/ol/layers';
import { OpendataBitmapTileLayer } from '@app/components/ol/layers';
import { OpendataVectorTileLayer } from '@app/components/ol/layers';
import { NetworkBitmapTileLayer } from '@app/components/ol/layers';
import { NetworkVectorTileLayer } from '@app/components/ol/layers';
import { MapLayer } from '@app/components/ol/layers';
import { OpenlayersMapService } from '@app/components/ol/services';
import { MapZoomService } from '@app/components/ol/services';
import { PoiTileLayerService } from '@app/components/ol/services';
import { MapService } from '@app/components/ol/services';
import { MapMode } from '@app/components/ol/services';
import { MainMapStyleParameters } from '@app/components/ol/style';
import { MainMapStyle } from '@app/components/ol/style';
import { selectPreferencesShowProposed } from '@app/core/preferences';
import { NetworkTypes } from '@app/kpn/common';
import { PoiService } from '@app/services';
import { Subscriptions } from '@app/util';
import { Store } from '@ngrx/store';
import Map from 'ol/Map';
import Overlay from 'ol/Overlay';
import View from 'ol/View';
import { Observable } from 'rxjs';
import { combineLatest } from 'rxjs';
import { skip } from 'rxjs';
import { map } from 'rxjs/operators';
import { PlannerInteraction } from '../../domain/interaction/planner-interaction';
import { PlannerService } from '../../planner.service';
import { PlannerStateService } from '../../services/planner-state.service';
import { actionPlannerPosition } from '../../store/planner-actions';
import { actionPlannerLayerStates } from '../../store/planner-actions';
import { actionPlannerMapFinalized } from '../../store/planner-actions';
import { selectPlannerMapMode } from '../../store/planner-selectors';

@Injectable()
export class PlannerMapService extends OpenlayersMapService {
  private overlay: Overlay;
  private readonly interaction = new PlannerInteraction(
    this.plannerService.engine
  );

  private parameters$: Observable<MainMapStyleParameters> = combineLatest([
    this.store.select(selectPlannerMapMode),
    this.store.select(selectPreferencesShowProposed),
    this.mapService.highlightedRouteId$,
    this.mapService.surveyDateInfo$,
  ]).pipe(
    map(([mapMode, showProposed, highlightedRouteId, surveyDateValues]) => {
      const selectedRouteId = '';
      const selectedNodeId = '';
      return new MainMapStyleParameters(
        mapMode,
        showProposed,
        surveyDateValues,
        selectedRouteId,
        selectedNodeId,
        highlightedRouteId
      );
    })
  );

  private networkVectorLayerStyle = new MainMapStyle(this.parameters$);

  private subcriptions = new Subscriptions();

  constructor(
    private plannerService: PlannerService,
    private plannerStateService: PlannerStateService,
    private poiService: PoiService,
    private mapZoomService: MapZoomService,
    private poiTileLayerService: PoiTileLayerService,
    private mapService: MapService,
    private store: Store
  ) {
    super();
  }

  init(
    networkType: NetworkType,
    mapMode: MapMode,
    resultMode: string,
    queryParams: Params
  ): void {
    this.subcriptions.unsubscribe();

    const initialPosition = this.plannerStateService.parsePosition(queryParams);

    this.overlay = this.buildOverlay();

    this.registerLayers(networkType, queryParams);
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

    this.map.getView().setZoom(initialPosition.zoom);
    this.map.getView().setCenter([initialPosition.x, initialPosition.y]);

    this.plannerService.init(this.map);
    this.interaction.addToMap(this.map);

    const view = this.map.getView();
    this.poiService.updateZoomLevel(view.getZoom()); // TODO can do better?
    this.mapZoomService.install(view); // TODO eliminate

    MapGeocoder.install(this.map);

    // if (this.planLoaded) {
    //   this.zoomInToRoute();
    // }
    this.finalizeSetup();

    this.store.dispatch(
      actionPlannerMapFinalized({
        position: this.mapPosition,
        layerStates: this.layerStates,
      })
    );

    this.subcriptions.add(
      this.mapPosition$
        .pipe(skip(1))
        .subscribe((mapPosition) =>
          this.store.dispatch(actionPlannerPosition({ mapPosition }))
        )
    );
    this.subcriptions.add(
      this.layerStates$
        .pipe(skip(1))
        .subscribe((layerStates) =>
          this.store.dispatch(actionPlannerLayerStates({ layerStates }))
        )
    );
  }

  override destroy() {
    // this.networkVectorLayerStyle.destroy(); can we really do this? consider the lifecycle of this service...
    this.subcriptions.unsubscribe();
    super.destroy();
  }

  handleNetworkChange(
    networkType: NetworkType,
    mapMode: MapMode,
    pois: boolean
  ) {
    let changed = false;
    const newLayerStates = this.layerStates.map((layerState) => {
      let enabled = layerState.enabled;
      const correspondingMapLayer = this.mapLayers.find(
        (mapLayer) => mapLayer.name === layerState.layerName
      );
      if (correspondingMapLayer && correspondingMapLayer.networkType) {
        enabled = correspondingMapLayer.networkType === networkType;
      }
      if (enabled !== layerState.enabled) {
        changed = true;
        const visible = layerState.layerName === networkType;
        return { ...layerState, visible, enabled };
      }
      return layerState;
    });
    if (changed) {
      this.updateLayerStates(newLayerStates);
      this.plannerUpdateLayerVisibility(networkType, mapMode, pois);
    }
  }

  plannerUpdateLayerVisibility(
    networkType: NetworkType,
    mapMode: MapMode,
    pois: boolean
  ): void {
    const layerStates: MapLayerState[] = this.layerStates;
    const zoom = this.mapPosition.zoom;

    this.mapLayers.forEach((mapLayer) => {
      let visible: boolean;
      if (mapLayer.name === PoiTileLayerService.poiLayerName) {
        visible = pois;
      } else if (
        !!mapLayer.networkType &&
        mapLayer.networkType !== networkType
      ) {
        visible = false;
      } else if (!!mapLayer.mapMode && mapLayer.mapMode !== mapMode) {
        visible = false;
      } else {
        const mapLayerState = layerStates.find(
          (layerState) => layerState.layerName === mapLayer.name
        );
        if (!mapLayerState) {
          visible = false;
        } else if (!mapLayerState.visible) {
          visible = false;
        } else {
          visible = zoom >= mapLayer.minZoom && zoom <= mapLayer.maxZoom;
          if (
            visible &&
            mapLayer.id.includes('vector') &&
            mapLayer.layer.getVisible()
          ) {
            mapLayer.layer.changed();
          }
        }
      }
      mapLayer.layer.setVisible(visible);
    });
  }

  private buildOverlay(): Overlay {
    return new Overlay({
      id: 'popup',
      element: document.getElementById('popup'),
      autoPan: true,
      autoPanAnimation: {
        duration: 250,
      },
    });
  }

  private registerLayers(networkType: NetworkType, queryParams: Params): void {
    let urlLayerNames: string[] = [];
    const layersParam = queryParams['layers'];
    if (layersParam) {
      urlLayerNames = layersParam.split(',');
    }

    const registry = new MapLayerRegistry();
    registry.register(urlLayerNames, BackgroundLayer.build(), true);
    registry.register(urlLayerNames, OsmLayer.build(), false);

    registry.registerAll(
      urlLayerNames,
      this.flandersOpenDataHikingLayers(),
      false,
      networkType === NetworkType.hiking
    );

    /* TODO planner this.allLayers.push(new OpendataBitmapTileLayer().build(NetworkType.cycling)); */
    /* TODO planner this.allLayers.push(new OpendataVectorTileLayer().build(NetworkType.cycling)); */

    registry.registerAll(
      urlLayerNames,
      this.netherlandsHikingOpenDataLayers(),
      false,
      networkType === NetworkType.hiking
    );

    NetworkTypes.all.forEach((layerNetworkType) => {
      registry.registerAll(
        urlLayerNames,
        this.networkLayers(layerNetworkType),
        layerNetworkType === networkType,
        layerNetworkType === networkType
      );
    });

    registry.register(urlLayerNames, TileDebug256Layer.build(), false);
    registry.register(urlLayerNames, TileDebug512Layer.build(), false);
    registry.register(
      urlLayerNames,
      this.poiTileLayerService.buildLayer(),
      false, // TODO derive from url params
      false
    );

    this.register(registry);
  }

  private flandersOpenDataHikingLayers(): MapLayer[] {
    return [
      new OpendataBitmapTileLayer().build(
        NetworkType.hiking,
        'flanders-hiking',
        'flanders/hiking'
      ),
      new OpendataVectorTileLayer().build(
        NetworkType.hiking,
        'flanders-hiking',
        'flanders/hiking'
      ),
    ];
  }

  private netherlandsHikingOpenDataLayers(): MapLayer[] {
    return [
      new OpendataBitmapTileLayer().build(
        NetworkType.hiking,
        'netherlands-hiking',
        'netherlands/hiking'
      ),
      new OpendataVectorTileLayer().build(
        NetworkType.hiking,
        'netherlands-hiking',
        'netherlands/hiking'
      ),
    ];
  }

  private networkLayers(networkType: NetworkType): MapLayer[] {
    return [
      NetworkBitmapTileLayer.build(networkType, 'surface'),
      NetworkBitmapTileLayer.build(networkType, 'survey'),
      NetworkBitmapTileLayer.build(networkType, 'analysis'),
      NetworkVectorTileLayer.build(
        networkType,
        this.networkVectorLayerStyle.styleFunction()
      ),
    ];
  }
}
