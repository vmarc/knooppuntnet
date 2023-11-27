import { Injectable } from '@angular/core';
import { Params } from '@angular/router';
import { NetworkType } from '@api/custom';
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
import { selectSharedSurveyDateInfo } from '@app/core';
import { selectPreferencesShowProposed } from '@app/core';
import { NetworkTypes } from '@app/kpn/common';
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
import { filter } from 'rxjs/operators';
import { map } from 'rxjs/operators';
import { PlannerInteraction } from '../../domain/interaction/planner-interaction';
import { PlannerService } from '../../planner.service';
import { MapService } from '../../services/map.service';
import { actionPlannerPosition } from '../../store/planner-actions';
import { actionPlannerLayerStates } from '../../store/planner-actions';
import { actionPlannerMapFinalized } from '../../store/planner-actions';
import { selectPlannerMapMode } from '../../store/planner-selectors';
import { PlannerState } from '../../store/planner-state';

@Injectable({
  providedIn: 'root',
})
export class PlannerMapService extends OpenlayersMapService {
  private readonly plannerPositionKey = 'planner-position';

  private readonly defaultPoiLayerStates: MapLayerState[] = [
    { layerName: 'hiking-biking', enabled: true, visible: true },
    { layerName: 'landmarks', enabled: true, visible: true },
    { layerName: 'restaurants', enabled: true, visible: true },
    { layerName: 'places-to-stay', enabled: true, visible: true },
    { layerName: 'tourism', enabled: true, visible: true },
    { layerName: 'amenity', enabled: true, visible: false },
    { layerName: 'shops', enabled: true, visible: false },
    { layerName: 'foodshops', enabled: true, visible: false },
    { layerName: 'sports', enabled: true, visible: false },
  ];

  private overlay: Overlay;
  private readonly interaction = new PlannerInteraction(this.plannerService.engine);

  private parameters$: Observable<MainMapStyleParameters> = combineLatest([
    this.store.select(selectPlannerMapMode),
    this.store.select(selectPreferencesShowProposed),
    this.mapService.highlightedRouteId$,
    this.store.select(selectSharedSurveyDateInfo).pipe(filter((x) => x !== null)),
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
    private poiService: PoiService,
    private mapZoomService: MapZoomService,
    private poiTileLayerService: PoiTileLayerService,
    private mapService: MapService,
    private browserStorageService: BrowserStorageService,
    private store: Store
  ) {
    super();
  }

  toQueryParams(state: PlannerState): Params {
    const position = MapPosition.toQueryParam(state.position);
    const mode = state.mapMode;
    const result = state.resultMode;
    const layers = state.layerStates
      .filter((ls) => ls.visible)
      .map((ls) => ls.layerName)
      .join(',');
    const poiLayers = state.poiLayerStates
      .filter((ls) => ls.visible)
      .map((ls) => ls.layerName)
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

    let urlLayerNames: string[] = [];
    const layersParam = queryParams['layers'];
    if (layersParam) {
      urlLayerNames = layersParam.split(',');
    }
    const layerStates = this.registerLayers(networkType, urlLayerNames);
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

  handleNetworkChange(networkType: NetworkType, mapMode: MapMode) {
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
      this.plannerUpdateLayerVisibility(networkType, mapMode);
    }
  }

  plannerUpdateLayerVisibility(networkType: NetworkType, mapMode: MapMode): void {
    const layerStates: MapLayerState[] = this.layerStates;
    const zoom = this.mapPosition.zoom;

    this.mapLayers.forEach((mapLayer) => {
      let visible: boolean;
      if (!!mapLayer.networkType && mapLayer.networkType !== networkType) {
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
          if (visible && mapLayer.id.includes('vector') && mapLayer.layer.getVisible()) {
            mapLayer.layer.changed();
          }
        }
      }
      mapLayer.layer.setVisible(visible);
    });
  }

  plannerUpdatePoiLayerVisibility(newLayerStates: MapLayerState[]): void {
    this.updateLayerStates(newLayerStates);
    this.mapLayers.forEach((mapLayer) => {
      if (mapLayer.name === PoiTileLayerService.poiLayerName) {
        const mapLayerState = this.layerStates.find(
          (layerState) => layerState.layerName === mapLayer.name
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

  private registerLayers(networkType: NetworkType, urlLayerNames: string[]): MapLayerState[] {
    const registry = new MapLayerRegistry();
    registry.register(urlLayerNames, BackgroundLayer.build(), true);
    registry.register(urlLayerNames, OsmLayer.build(), false);

    registry.registerAll(
      urlLayerNames,
      this.flandersOpenDataHikingLayers(),
      false,
      networkType === NetworkType.hiking
    );

    registry.registerAll(
      urlLayerNames,
      this.flandersOpenDataCyclingLayers(),
      false,
      networkType === NetworkType.cycling
    );

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
    registry.register(urlLayerNames, this.poiTileLayerService.buildLayer(), true, false);

    this.register(registry);
    return registry.layerStates;
  }

  private flandersOpenDataHikingLayers(): MapLayer[] {
    return [
      new OpendataBitmapTileLayer().build(NetworkType.hiking, 'flanders-hiking', 'flanders/hiking'),
      new OpendataVectorTileLayer().build(NetworkType.hiking, 'flanders-hiking', 'flanders/hiking'),
    ];
  }

  private flandersOpenDataCyclingLayers(): MapLayer[] {
    return [
      new OpendataBitmapTileLayer().build(
        NetworkType.cycling,
        'flanders-cycling',
        'flanders/cycling'
      ),
      new OpendataVectorTileLayer().build(
        NetworkType.cycling,
        'flanders-cycling',
        'flanders/cycling'
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
        const visible = poiLayersParam.includes(defaultLayerState.layerName);
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
