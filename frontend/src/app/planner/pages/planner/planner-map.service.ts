import { inject } from '@angular/core';
import { Injectable } from '@angular/core';
import { Params } from '@angular/router';
import { NetworkType } from '@api/custom';
import { PreferencesService } from '@app/core';
import { selectSharedSurveyDateInfo } from '@app/core';
import { NetworkTypes } from '@app/kpn/common';
import { MapPosition } from '@app/ol/domain';
import { ZoomLevel } from '@app/ol/domain';
import { MapGeocoder } from '@app/ol/domain';
import { MapLayerState } from '@app/ol/domain';
import { MapControls } from '@app/ol/layers';
import { MapLayerRegistry } from '@app/ol/layers';
import { BackgroundLayer } from '@app/ol/layers';
import { OsmLayer } from '@app/ol/layers';
import { TileDebug256Layer } from '@app/ol/layers';
import { TileDebug512Layer } from '@app/ol/layers';
import { OpendataBitmapTileLayer } from '@app/ol/layers';
import { OpendataVectorTileLayer } from '@app/ol/layers';
import { NetworkBitmapTileLayer } from '@app/ol/layers';
import { NetworkVectorTileLayer } from '@app/ol/layers';
import { MapLayer } from '@app/ol/layers';
import { OpenlayersMapService } from '@app/ol/services';
import { MapZoomService } from '@app/ol/services';
import { PoiTileLayerService } from '@app/ol/services';
import { MapMode } from '@app/ol/services';
import { MainMapStyleParameters } from '@app/ol/style';
import { MainMapStyle } from '@app/ol/style';
import { BrowserStorageService } from '@app/services';
import { PoiService } from '@app/services';
import { Subscriptions } from '@app/util';
import { Store } from '@ngrx/store';
import { Coordinate } from 'ol/coordinate';
import Map from 'ol/Map';
import Overlay from 'ol/Overlay';
import { fromLonLat } from 'ol/proj';
import View from 'ol/View';
import { Observable } from 'rxjs';
import { combineLatest } from 'rxjs';
import { skip } from 'rxjs';
import { tap } from 'rxjs/operators';
import { filter } from 'rxjs/operators';
import { map } from 'rxjs/operators';
import { PlannerInteraction } from '../../domain/interaction/planner-interaction';
import { PlannerService } from '../../planner.service';
import { MapService } from '../../services/map.service';
import { actionPlannerPosition } from '../../store/planner-actions';
import { actionPlannerLayerStates } from '../../store/planner-actions';
import { actionPlannerMapFinalized } from '../../store/planner-actions';
import { selectPlannerNetworkType } from '../../store/planner-selectors';
import { selectPlannerMapMode } from '../../store/planner-selectors';
import { PlannerState } from '../../store/planner-state';

@Injectable({
  providedIn: 'root',
})
export class PlannerMapService extends OpenlayersMapService {
  private readonly plannerService = inject(PlannerService);
  private readonly poiService = inject(PoiService);
  private readonly mapZoomService = inject(MapZoomService);
  private readonly poiTileLayerService = inject(PoiTileLayerService);
  private readonly mapService = inject(MapService);
  private readonly browserStorageService = inject(BrowserStorageService);
  private readonly store = inject(Store);
  private readonly preferencesService = inject(PreferencesService);

  private readonly plannerPositionKey = 'planner-position';

  private readonly defaultPoiLayerStates: MapLayerState[] = [
    { id: 'hiking-biking', name: 'TODO TRANSLATION', enabled: true, visible: true },
    { id: 'landmarks', name: 'TODO TRANSLATION', enabled: true, visible: true },
    { id: 'restaurants', name: 'TODO TRANSLATION', enabled: true, visible: true },
    { id: 'places-to-stay', name: 'TODO TRANSLATION', enabled: true, visible: true },
    { id: 'tourism', name: 'TODO TRANSLATION', enabled: true, visible: true },
    { id: 'amenity', name: 'TODO TRANSLATION', enabled: true, visible: false },
    { id: 'shops', name: 'TODO TRANSLATION', enabled: true, visible: false },
    { id: 'foodshops', name: 'TODO TRANSLATION', enabled: true, visible: false },
    { id: 'sports', name: 'TODO TRANSLATION', enabled: true, visible: false },
  ];

  private overlay: Overlay;
  private readonly interaction = new PlannerInteraction(this.plannerService.engine);

  private networkType: NetworkType;
  private mapMode: MapMode;

  private parameters$: Observable<MainMapStyleParameters> = combineLatest([
    this.store.select(selectPlannerMapMode),
    this.mapService.highlightedRouteId$,
    this.store.select(selectSharedSurveyDateInfo).pipe(filter((x) => x !== null)),
  ]).pipe(
    map(([mapMode, highlightedRouteId, surveyDateValues]) => {
      const selectedRouteId = '';
      const selectedNodeId = '';
      const showProposed = this.preferencesService.showProposed();
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

  toQueryParams(state: PlannerState): Params {
    const position = MapPosition.toQueryParam(state.position);
    const mode = state.mapMode;
    const result = state.resultMode;
    const layers = state.layerStates
      .filter((layerState) => layerState.visible)
      .map((layerState) => layerState.id)
      .join(',');
    const poiLayers = state.poiLayerStates
      .filter((layerState) => layerState.visible)
      .map((layerState) => layerState.id)
      .join(',');

    return {
      mode,
      position,
      result,
      layers,
      'poi-layers': poiLayers,
    };
  }

  toPlannerState(routeParams: Params, queryParams: Params): PlannerState {
    const networkType = this.parseNetworkType(routeParams);
    const position = this.parsePosition(queryParams);
    const mapMode = this.parseMapMode(queryParams);
    const resultMode = this.parseResultMode(queryParams);

    let urlLayerIds: string[] = [];
    const layersParam = queryParams['layers'];
    if (layersParam) {
      urlLayerIds = layersParam.split(',');
    }
    const layerStates = this.registerLayers(networkType, urlLayerIds);
    const poiLayerStates = this.parsePoiLayerStates(queryParams);

    return {
      networkType,
      position,
      mapMode,
      resultMode,
      layerStates,
      poiLayerStates,
    };
  }

  init(state: PlannerState): void {
    this.subcriptions.unsubscribe();

    this.subcriptions.add(
      this.store
        .select(selectPlannerMapMode)
        .pipe(tap((mapMode: MapMode) => (this.mapMode = mapMode)))
        .subscribe()
    );
    this.subcriptions.add(
      this.store
        .select(selectPlannerNetworkType)
        .pipe(tap((networkType: NetworkType) => (this.networkType = networkType)))
        .subscribe()
    );

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

    this.store.dispatch(
      actionPlannerMapFinalized({
        position: this.mapPosition,
        layerStates: this.layerStates,
      })
    );

    this.subcriptions.add(
      this.mapPosition$
        .pipe(skip(1))
        .subscribe((mapPosition) => this.store.dispatch(actionPlannerPosition({ mapPosition })))
    );
    this.subcriptions.add(
      this.layerStates$
        .pipe(skip(1))
        .subscribe((layerStates) => this.store.dispatch(actionPlannerLayerStates({ layerStates })))
    );
  }

  override destroy() {
    // this.networkVectorLayerStyle.destroy(); can we really do this? consider the lifecycle of this service...
    this.subcriptions.unsubscribe();
    super.destroy();
  }

  networkTypeChanged(networkType: NetworkType) {
    let changed = false;
    const newLayerStates = this.layerStates.map((layerState) => {
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
        const mapLayerState = this.layerStates.find((layerState) => layerState.id === mapLayer.id);
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

  private registerLayers(networkType: NetworkType, urlLayerIds: string[]): MapLayerState[] {
    const registry = new MapLayerRegistry();
    registry.register(urlLayerIds, BackgroundLayer.build(), true);
    registry.register(urlLayerIds, OsmLayer.build(), false);

    registry.registerAll(
      urlLayerIds,
      this.flandersOpenDataHikingLayers(),
      false,
      networkType === NetworkType.hiking
    );

    registry.registerAll(
      urlLayerIds,
      this.flandersOpenDataCyclingLayers(),
      false,
      networkType === NetworkType.cycling
    );

    registry.registerAll(
      urlLayerIds,
      this.netherlandsHikingOpenDataLayers(),
      false,
      networkType === NetworkType.hiking
    );

    NetworkTypes.all.forEach((layerNetworkType) => {
      registry.registerAll(
        urlLayerIds,
        this.networkLayers(layerNetworkType),
        layerNetworkType === networkType,
        layerNetworkType === networkType
      );
    });

    registry.register(urlLayerIds, TileDebug256Layer.build(), false);
    registry.register(urlLayerIds, TileDebug512Layer.build(), false);
    registry.register(urlLayerIds, this.poiTileLayerService.buildLayer(), true, false);

    this.register(registry);
    return registry.layerStates;
  }

  private flandersOpenDataHikingLayers(): MapLayer[] {
    const name = $localize`:@@map.layer.flanders-hiking:Toerisme Vlaanderen`;
    return [
      OpendataBitmapTileLayer.build(NetworkType.hiking, 'flanders-hiking', name, 'flanders/hiking'),
      OpendataVectorTileLayer.build(NetworkType.hiking, 'flanders-hiking', name, 'flanders/hiking'),
    ];
  }

  private flandersOpenDataCyclingLayers(): MapLayer[] {
    const name = $localize`:@@map.layer.flanders-cycling:Toerisme Vlaanderen`;
    return [
      OpendataBitmapTileLayer.build(
        NetworkType.cycling,
        'flanders-cycling',
        name,
        'flanders/cycling'
      ),
      OpendataVectorTileLayer.build(
        NetworkType.cycling,
        'flanders-cycling',
        name,
        'flanders/cycling'
      ),
    ];
  }

  private netherlandsHikingOpenDataLayers(): MapLayer[] {
    const name = $localize`:@@map.layer.netherlands-hiking:Netherlands routedatabank`;
    return [
      OpendataBitmapTileLayer.build(
        NetworkType.hiking,
        'netherlands-hiking',
        name,
        'netherlands/hiking'
      ),
      OpendataVectorTileLayer.build(
        NetworkType.hiking,
        'netherlands-hiking',
        name,
        'netherlands/hiking'
      ),
    ];
  }

  private networkLayers(networkType: NetworkType): MapLayer[] {
    return [
      NetworkBitmapTileLayer.build(networkType, 'surface'),
      NetworkBitmapTileLayer.build(networkType, 'survey'),
      NetworkBitmapTileLayer.build(networkType, 'analysis'),
      NetworkVectorTileLayer.build(networkType, this.networkVectorLayerStyle.styleFunction()),
    ];
  }

  private parseNetworkType(queryParams: Params): NetworkType {
    const networkTypeParam = queryParams['networkType'];
    if (networkTypeParam) {
      const networkType = NetworkTypes.withName(networkTypeParam);
      if (networkType) {
        return networkType;
      }
    }
    return NetworkType.hiking;
  }

  private parsePosition(queryParams: Params): MapPosition {
    const positionParam = queryParams['position'];
    let position = MapPosition.fromQueryParam(positionParam);
    if (!position) {
      const mapPositionString = this.browserStorageService.get(this.plannerPositionKey);
      if (mapPositionString) {
        position = MapPosition.fromQueryParam(mapPositionString);
      } else {
        // TODO replace temporary code
        const a: Coordinate = fromLonLat([2.24, 50.16]);
        const b: Coordinate = fromLonLat([10.56, 54.09]);
        // const extent: Extent = [a[0], a[1], b[0], b[1]];
        const x = (b[0] - a[0]) / 2 + a[0];
        const y = (b[1] - a[1]) / 2 + a[1];
        position = new MapPosition(14, x, y, 0);
      }
    }
    return position;
  }

  private parseMapMode(queryParams: Params): MapMode {
    const mapModeParam = queryParams['mode'];
    let mapMode: MapMode = 'surface';
    if (mapModeParam === 'survey') {
      mapMode = 'survey';
    } else if (mapModeParam === 'analysis') {
      mapMode = 'analysis';
    }
    return mapMode;
  }

  private parseResultMode(queryParams: Params): string {
    const resultModeParam = queryParams['result'];
    let resultMode = 'compact';
    if (resultModeParam === 'details') {
      resultMode = 'details';
    }
    return resultMode;
  }

  private parsePoiLayerStates(queryParams: Params): MapLayerState[] {
    const poiLayersParam = queryParams['poi-layers'];
    let poiLayerStates: MapLayerState[];
    if (poiLayersParam) {
      poiLayerStates = this.defaultPoiLayerStates.map((defaultLayerState) => {
        const visible = poiLayersParam.includes(defaultLayerState.id);
        return {
          ...defaultLayerState,
          visible,
        };
      });
    } else {
      poiLayerStates = this.defaultPoiLayerStates;
    }
    return poiLayerStates;
  }
}
