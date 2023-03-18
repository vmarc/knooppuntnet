import { Injectable } from '@angular/core';
import { NetworkType } from '@api/custom/network-type';
import { MapLayerState } from '@app/components/ol/domain/map-layer-state';
import { ZoomLevel } from '@app/components/ol/domain/zoom-level';
import { MapLayer } from '@app/components/ol/layers/map-layer';
import { MapLayerChange } from '@app/components/ol/layers/map-layer-change';
import { OpendataTileLayer } from '@app/components/ol/layers/opendata-tile-layer';
import { MapLayerService } from '@app/components/ol/services/map-layer.service';
import { MapMode } from '@app/components/ol/services/map-mode';
import { MapZoomService } from '@app/components/ol/services/map-zoom.service';
import { MapService } from '@app/components/ol/services/map.service';
import { PoiTileLayerService } from '@app/components/ol/services/poi-tile-layer.service';
import { MainMapStyle } from '@app/components/ol/style/main-map-style';
import { selectPreferencesExtraLayers } from '@app/core/preferences/preferences.selectors';
import { selectPreferencesShowProposed } from '@app/core/preferences/preferences.selectors';
import { NetworkTypes } from '@app/kpn/common/network-types';
import { Subscriptions } from '@app/util/Subscriptions';
import { Store } from '@ngrx/store';
import { List, Map as ImmutableMap } from 'immutable';
import VectorTileLayer from 'ol/layer/VectorTile';
import Map from 'ol/Map';
import { first } from 'rxjs';
import { Observable } from 'rxjs';
import { PlannerService } from './planner.service';

@Injectable()
export class PlannerLayerService {
  standardLayers: List<MapLayer>;
  private activeNetworkLayer: MapLayer = null;
  private activeFlandersLayer: MapLayer = null;
  private activeNetherlandsLayer: MapLayer = null;
  /*private*/
  osmLayer: MapLayer;
  private backgroundLayer: MapLayer;
  private tile256NameLayer: MapLayer;
  private tile512NameLayer: MapLayer;
  private poiLayer: MapLayer;
  private flandersHikingVectorLayer: MapLayer;
  private netherlandsHikingVectorLayer: MapLayer;
  private bitmapLayersSurface: ImmutableMap<NetworkType, MapLayer>;
  private bitmapLayersSurvey: ImmutableMap<NetworkType, MapLayer>;
  private bitmapLayersAnalysis: ImmutableMap<NetworkType, MapLayer>;
  private vectorLayers: ImmutableMap<NetworkType, MapLayer>;
  public allLayers: List<MapLayer>;

  private readonly mapRelatedSubscriptions = new Subscriptions();

  private readonly extraLayers$: Observable<boolean> = this.store.select(
    selectPreferencesExtraLayers
  );

  constructor(
    private mapLayerService: MapLayerService,
    private poiTileLayerService: PoiTileLayerService,
    private plannerService: PlannerService,
    private mapService: MapService,
    private mapZoomService: MapZoomService,
    private store: Store
  ) {}

  mapInit(olMap: Map) {
    this.allLayers.forEach((mapLayer) => {
      if (mapLayer.applyMap) {
        mapLayer.applyMap(olMap);
      }
    });

    const mainMapStyle = new MainMapStyle(
      olMap,
      this.mapService,
      this.store
    ).styleFunction();
    List(this.vectorLayers.values()).forEach((mapLayer) => {
      const vectorTileLayer = mapLayer.layer as VectorTileLayer;
      vectorTileLayer.setStyle(mainMapStyle);
      this.mapRelatedSubscriptions.add(
        this.mapService.mapMode$.subscribe(() =>
          vectorTileLayer.getSource().changed()
        ),
        this.store
          .select(selectPreferencesShowProposed)
          .subscribe(() => vectorTileLayer.getSource().changed())
      );
    });
  }

  mapDestroy(olMap: Map): void {
    this.activeNetworkLayer = null;
    this.activeFlandersLayer = null;
    this.mapRelatedSubscriptions.unsubscribe();
  }

  initializeLayers(): void {
    this.osmLayer = this.mapLayerService.osmLayer();
    // this.backgroundLayer = this.mapLayerService.backgroundLayer('main-map');
    this.tile256NameLayer = this.mapLayerService.tile256NameLayer();
    this.tile512NameLayer = this.mapLayerService.tile512NameLayer();
    this.poiLayer = this.poiTileLayerService.buildLayer();

    this.flandersHikingVectorLayer = new OpendataTileLayer().build(
      NetworkType.hiking,
      'flanders-hiking',
      'flanders/hiking'
    );
    /* TODO this.flandersCyclingLayer = new OpendataTileLayer().build(NetworkType.cycling); */
    this.netherlandsHikingVectorLayer = new OpendataTileLayer().build(
      NetworkType.hiking,
      'netherlands-hiking',
      'netherlands/hiking'
    );

    this.bitmapLayersSurface = this.buildBitmapLayers('surface');
    this.bitmapLayersSurvey = this.buildBitmapLayers('survey');
    this.bitmapLayersAnalysis = this.buildBitmapLayers('analysis');
    this.vectorLayers = this.buildVectorLayers();

    this.extraLayers$.pipe(first()).subscribe((extraLayers) => {
      if (extraLayers) {
        this.standardLayers = List([
          this.osmLayer,
          // this.backgroundLayer,
          this.tile256NameLayer,
          this.tile512NameLayer,
          this.poiLayer,
        ]);
      } else {
        this.standardLayers = List([/*this.backgroundLayer,*/ this.poiLayer]);
      }
    });

    this.allLayers = this.standardLayers
      .concat(this.bitmapLayersSurface.values())
      .concat(this.bitmapLayersSurvey.values())
      .concat(this.bitmapLayersAnalysis.values())
      .concat(this.vectorLayers.values())
      .concat([this.flandersHikingVectorLayer])
      .concat([this.netherlandsHikingVectorLayer]);
  }

  updateLayerVisibility(
    layerStates: MapLayerState[],
    networkType: NetworkType,
    mapMode: MapMode,
    zoom: number
  ): void {
    console.log(
      `updateLayerVisibility(zoom=${zoom}, networkType=${networkType}, mapMode=${mapMode})`
    );
    this.allLayers.forEach((mapLayer) => {
      let visible = true;
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
        } else {
          if (
            !mapLayerState.visible &&
            (zoom < mapLayer.minZoom || zoom > mapLayer.maxZoom)
          ) {
            visible = false;
          }
        }
      }
      mapLayer.layer.setVisible(visible);
    });

    const visibleLayers = this.allLayers
      .filter((mapLayer) => mapLayer.layer.getVisible())
      .map((mapLayer) => mapLayer.name)
      .join(',');
    console.log(`visibleLayers=${visibleLayers}`);
  }

  updateSize(): void {
    // this.backgroundLayer.updateSize();
  }

  private networkLayerChange(
    zoomLevel: number,
    networkType: NetworkType,
    mapMode: MapMode
  ): MapLayerChange {
    const newLayer = this.determineNetworkLayer(
      zoomLevel,
      networkType,
      mapMode
    );
    const oldLayer = this.activeNetworkLayer;
    this.activeNetworkLayer = newLayer;
    return new MapLayerChange(oldLayer, newLayer);
  }

  private flandersLayerChange(
    zoomLevel: number,
    networkType: NetworkType
  ): MapLayerChange {
    let newLayer: MapLayer = null;
    if (zoomLevel >= ZoomLevel.bitmapTileMinZoom) {
      if (networkType === NetworkType.hiking) {
        newLayer = this.flandersHikingVectorLayer;
      }
    }
    const oldLayer = this.activeFlandersLayer;
    this.activeFlandersLayer = newLayer;
    return new MapLayerChange(oldLayer, newLayer);
  }

  private netherlandsLayerChange(
    zoomLevel: number,
    networkType: NetworkType
  ): MapLayerChange {
    let newLayer: MapLayer = null;
    if (zoomLevel >= ZoomLevel.bitmapTileMinZoom) {
      if (networkType === NetworkType.hiking) {
        newLayer = this.netherlandsHikingVectorLayer;
      }
    }
    const oldLayer = this.activeNetherlandsLayer;
    this.activeNetherlandsLayer = newLayer;
    return new MapLayerChange(oldLayer, newLayer);
  }

  private determineNetworkLayer(
    zoomLevel: number,
    networkType: NetworkType,
    mapMode: MapMode
  ): MapLayer {
    let newLayer: MapLayer;
    if (zoomLevel <= ZoomLevel.bitmapTileMaxZoom) {
      if (mapMode === 'surface') {
        newLayer = this.bitmapLayersSurface.get(networkType);
      } else if (mapMode === 'survey') {
        newLayer = this.bitmapLayersSurvey.get(networkType);
      } else {
        newLayer = this.bitmapLayersAnalysis.get(networkType);
      }
    } else if (zoomLevel >= ZoomLevel.vectorTileMinZoom) {
      newLayer = this.vectorLayers.get(networkType);
    }
    return newLayer;
  }

  private buildBitmapLayers(
    mapMode: MapMode
  ): ImmutableMap<NetworkType, MapLayer> {
    const keysAndValues: List<[NetworkType, MapLayer]> = List(
      NetworkTypes.all
    ).map((networkType) => [
      networkType,
      this.mapLayerService.networkBitmapTileLayer(networkType, mapMode),
    ]);
    return ImmutableMap<NetworkType, MapLayer>(keysAndValues.toArray());
  }

  private buildVectorLayers(): ImmutableMap<NetworkType, MapLayer> {
    const keyAndValues: List<[NetworkType, MapLayer]> = List(
      NetworkTypes.all
    ).map((networkType) => [
      networkType,
      this.mapLayerService.networkVectorTileLayer(networkType),
    ]);
    return ImmutableMap<NetworkType, MapLayer>(keyAndValues.toArray());
  }
}
