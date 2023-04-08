import { Injectable } from '@angular/core';
import { OpenlayersMapService } from '@app/components/ol/services/openlayers-map-service';
import { MapLayerRegistry } from '@app/components/ol/layers/map-layer-registry';
import { BackgroundLayer } from '@app/components/ol/layers/background-layer';
import { OsmLayer } from '@app/components/ol/layers/osm-layer';
import Overlay from 'ol/Overlay';
import { MapControls } from '@app/components/ol/layers/map-controls';
import View from 'ol/View';
import { ZoomLevel } from '@app/components/ol/domain/zoom-level';
import { MapGeocoder } from '@app/components/ol/domain/map-geocoder';
import Map from 'ol/Map';
import { PlannerService } from '@app/planner/services/planner.service';
import { PlannerInteraction } from '@app/planner/domain/interaction/planner-interaction';
import { PoiService } from '@app/services/poi.service';
import { MapZoomService } from '@app/components/ol/services/map-zoom.service';
import { PlannerPositionService } from '@app/planner/services/planner-position.service';
import { TileDebug256Layer } from '@app/components/ol/layers/tile-debug-256-layer';
import { TileDebug512Layer } from '@app/components/ol/layers/tile-debug-512-layer';
import { OpendataBitmapTileLayer } from '@app/components/ol/layers/opendata-bitmap-tile-layer';
import { NetworkType } from '@api/custom/network-type';
import { OpendataVectorTileLayer } from '@app/components/ol/layers/opendata-vector-tile-layer';
import { NetworkTypes } from '@app/kpn/common/network-types';
import { NetworkBitmapTileLayer } from '@app/components/ol/layers/network-bitmap-tile-layer';
import { NetworkVectorTileLayer } from '@app/components/ol/layers/network-vector-tile-layer';
import { PoiTileLayerService } from '@app/components/ol/services/poi-tile-layer.service';
import { Observable } from 'rxjs';
import { combineLatest } from 'rxjs';
import { MainMapStyleParameters } from '@app/components/ol/style/main-map-style-parameters';
import { selectPlannerMapMode } from '@app/planner/store/planner-selectors';
import { selectPreferencesShowProposed } from '@app/core/preferences/preferences.selectors';
import { map } from 'rxjs/operators';
import { MainMapStyle } from '@app/components/ol/style/main-map-style';
import { Store } from '@ngrx/store';
import { MapService } from '@app/components/ol/services/map.service';
import { MapLayerState } from '@app/components/ol/domain/map-layer-state';
import { MapMode } from '@app/components/ol/services/map-mode';
import { PlannerState } from '@app/planner/store/planner-state';
import { MapLayer } from '@app/components/ol/layers/map-layer';

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

  constructor(
    private plannerService: PlannerService,
    private poiService: PoiService,
    private mapZoomService: MapZoomService,
    private positionService: PlannerPositionService,
    private poiTileLayerService: PoiTileLayerService,
    private mapService: MapService,
    private store: Store
  ) {
    super();
  }

  init(plannerState: PlannerState): void {
    this.overlay = this.buildOverlay();
    this.registerLayers(plannerState);
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

    this.map.getView().setZoom(plannerState.position.zoom);
    this.map
      .getView()
      .setCenter([plannerState.position.x, plannerState.position.y]);

    this.plannerService.init(this.map);
    this.interaction.addToMap(this.map);

    const view = this.map.getView();
    this.positionService.install(view); // TODO eliminate
    this.poiService.updateZoomLevel(view.getZoom()); // TODO can do better?
    this.mapZoomService.install(view); // TODO eliminate

    MapGeocoder.install(this.map);

    // if (this.planLoaded) {
    //   this.zoomInToRoute();
    // }
    this.finalizeSetup();
  }

  override destroy() {
    // this.networkVectorLayerStyle.destroy(); can we really do this? consider the lifecycle of this service...
    super.destroy();
  }

  handleNetworkChange(networkType: NetworkType) {
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

  private registerLayers(plannerState: PlannerState): void {
    const registry = new MapLayerRegistry();
    registry.register(BackgroundLayer.build(), true);
    registry.register(OsmLayer.build(), false);

    registry.registerAll(
      this.flandersOpenDataHikingLayers(),
      false, // TODO derive from params
      plannerState.networkType === NetworkType.hiking
    );

    /* TODO planner this.allLayers.push(new OpendataBitmapTileLayer().build(NetworkType.cycling)); */
    /* TODO planner this.allLayers.push(new OpendataVectorTileLayer().build(NetworkType.cycling)); */

    registry.registerAll(
      this.netherlandsHikingOpenDataLayers(),
      false, // TODO derive from params
      plannerState.networkType === NetworkType.hiking
    );

    NetworkTypes.all.forEach((networkType) => {
      registry.registerAll(
        this.networkLayers(networkType),
        plannerState.networkType === networkType, // TODO derive from params
        plannerState.networkType === networkType
      );
    });

    registry.register(TileDebug256Layer.build(), false);
    registry.register(TileDebug512Layer.build(), false);
    registry.register(
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
