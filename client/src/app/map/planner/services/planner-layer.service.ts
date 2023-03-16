import { Injectable } from '@angular/core';
import { NetworkType } from '@api/custom/network-type';
import { Store } from '@ngrx/store';
import { List, Map as ImmutableMap } from 'immutable';
import { Geometry } from 'ol/geom';
import VectorLayer from 'ol/layer/Vector';
import VectorTileLayer from 'ol/layer/VectorTile';
import Map from 'ol/Map';
import VectorSource from 'ol/source/Vector';
import { BehaviorSubject } from 'rxjs';
import { combineLatest } from 'rxjs';
import { Observable } from 'rxjs';
import { filter, first, map, tap } from 'rxjs/operators';
import { ZoomLevel } from '@app/components/ol/domain/zoom-level';
import { MapLayer } from '@app/components/ol/layers/map-layer';
import { MapLayerChange } from '@app/components/ol/layers/map-layer-change';
import { MapLayers } from '@app/components/ol/layers/map-layers';
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
import { PlannerService } from '../../planner.service';

@Injectable()
export class PlannerLayerService {
  standardLayers: List<MapLayer>;
  layerSwitcherMapLayers$: Observable<MapLayers>;
  gpxVectorLayer: VectorLayer<VectorSource<Geometry>>;
  private _layerSwitcherMapLayers$ = new BehaviorSubject<MapLayers>(
    new MapLayers(List([]))
  );
  private networkLayerChange$: Observable<MapLayerChange>;
  private flandersLayerChange$: Observable<MapLayerChange>;
  private netherlandsLayerChange$: Observable<MapLayerChange>;
  private activeNetworkLayer: MapLayer = null;
  private activeFlandersLayer: MapLayer = null;
  private activeNetherlandsLayer: MapLayer = null;
  private osmLayer: MapLayer;
  private backgroundLayer: MapLayer;
  private tile256NameLayer: MapLayer;
  private tile512NameLayer: MapLayer;
  private poiLayer: MapLayer;
  private gpxLayer: MapLayer;
  private flandersHikingVectorLayer: MapLayer;
  private netherlandsHikingVectorLayer: MapLayer;
  private bitmapLayersSurface: ImmutableMap<NetworkType, MapLayer>;
  private bitmapLayersSurvey: ImmutableMap<NetworkType, MapLayer>;
  private bitmapLayersAnalysis: ImmutableMap<NetworkType, MapLayer>;
  private vectorLayers: ImmutableMap<NetworkType, MapLayer>;
  private allLayers: List<MapLayer>;

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
  ) {
    this.layerSwitcherMapLayers$ = this._layerSwitcherMapLayers$.asObservable();

    this.networkLayerChange$ = combineLatest([
      this.mapZoomService.zoomLevel$,
      this.plannerService.context.networkType$,
      this.mapService.mapMode$,
    ]).pipe(
      map(([zoomLevel, networkType, mapMode]) => {
        const change = this.networkLayerChange(zoomLevel, networkType, mapMode);
        let layers = this.standardLayers.push(change.newLayer);
        if (zoomLevel >= ZoomLevel.vectorTileMinZoom) {
          if (networkType === NetworkType.hiking) {
            layers.push(this.flandersHikingVectorLayer);
            layers.push(this.netherlandsHikingVectorLayer);
          }
        }
        this._layerSwitcherMapLayers$.next(new MapLayers(layers));
        return change;
      })
    );

    this.flandersLayerChange$ = combineLatest([
      this.mapZoomService.zoomLevel$,
      this.plannerService.context.networkType$,
    ]).pipe(
      map(([zoomLevel, networkType]) =>
        this.flandersLayerChange(zoomLevel, networkType)
      ),
      tap((change) => {
        if (!!change.newLayer) {
          this._layerSwitcherMapLayers$.next(
            new MapLayers(
              this._layerSwitcherMapLayers$.value.layers.push(change.newLayer)
            )
          );
        }
      })
    );

    this.netherlandsLayerChange$ = combineLatest([
      this.mapZoomService.zoomLevel$,
      this.plannerService.context.networkType$,
    ]).pipe(
      map(([zoomLevel, networkType]) =>
        this.netherlandsLayerChange(zoomLevel, networkType)
      ),
      tap((change) => {
        if (!!change.newLayer) {
          this._layerSwitcherMapLayers$.next(
            new MapLayers(
              this._layerSwitcherMapLayers$.value.layers.push(change.newLayer)
            )
          );
        }
      })
    );
  }

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

    this.mapRelatedSubscriptions.add(
      this.networkLayerChange$
        .pipe(
          filter((change) => change.oldLayer?.name !== change.newLayer?.name),
          tap((change) => {
            if (change.oldLayer !== null) {
              olMap.removeLayer(change.oldLayer.layer);
            }
            if (!!change.newLayer) {
              olMap
                .getLayers()
                .insertAt(this.standardLayers.size, change.newLayer.layer);
            }
          })
        )
        .subscribe()
    );
    this.mapRelatedSubscriptions.add(
      this.flandersLayerChange$
        .pipe(
          filter((change) => !!change.oldLayer || !!change.newLayer),
          filter((change) => change.oldLayer?.name !== change.newLayer?.name),
          tap((change) => {
            if (change.oldLayer !== null) {
              olMap.removeLayer(change.oldLayer.layer);
            }
            if (!!change.newLayer) {
              olMap
                .getLayers()
                .insertAt(this.standardLayers.size, change.newLayer.layer);
            }
          })
        )
        .subscribe()
    );
    this.mapRelatedSubscriptions.add(
      this.netherlandsLayerChange$
        .pipe(
          filter((change) => !!change.oldLayer || !!change.newLayer),
          filter((change) => change.oldLayer?.name !== change.newLayer?.name),
          tap((change) => {
            if (change.oldLayer !== null) {
              olMap.removeLayer(change.oldLayer.layer);
            }
            if (!!change.newLayer) {
              olMap
                .getLayers()
                .insertAt(this.standardLayers.size, change.newLayer.layer);
            }
          })
        )
        .subscribe()
    );
  }

  mapDestroy(olMap: Map): void {
    this.activeNetworkLayer = null;
    this.activeFlandersLayer = null;
    this.mapRelatedSubscriptions.unsubscribe();
  }

  initializeLayers(): void {
    this.osmLayer = this.mapLayerService.osmLayer();
    this.backgroundLayer = this.mapLayerService.backgroundLayer('main-map');
    this.tile256NameLayer = this.mapLayerService.tile256NameLayer();
    this.tile512NameLayer = this.mapLayerService.tile512NameLayer();
    this.poiLayer = this.poiTileLayerService.buildLayer();
    this.gpxLayer = this.mapLayerService.gpxLayer();
    this.gpxVectorLayer = this.gpxLayer.layer as VectorLayer<
      VectorSource<Geometry>
    >;

    this.flandersHikingVectorLayer = new OpendataTileLayer().build(
      NetworkType.hiking,
      'flanders-hiking-layer',
      'flanders/hiking',
      $localize`:@@map.layer.opendata.flanders:Toerisme Vlaanderen`
    );
    /* TODO this.flandersCyclingLayer = new OpendataTileLayer().build(NetworkType.cycling); */
    this.netherlandsHikingVectorLayer = new OpendataTileLayer().build(
      NetworkType.hiking,
      'netherlands-hiking-layer',
      'netherlands/hiking',
      $localize`:@@map.layer.opendata.netherlands:Routedatabank Nederland`
    );

    this.bitmapLayersSurface = this.buildBitmapLayers(MapMode.surface);
    this.bitmapLayersSurvey = this.buildBitmapLayers(MapMode.survey);
    this.bitmapLayersAnalysis = this.buildBitmapLayers(MapMode.analysis);
    this.vectorLayers = this.buildVectorLayers();

    this.extraLayers$.pipe(first()).subscribe((extraLayers) => {
      if (extraLayers) {
        this.standardLayers = List([
          this.osmLayer,
          this.backgroundLayer,
          this.tile256NameLayer,
          this.tile512NameLayer,
          this.poiLayer,
          this.gpxLayer,
        ]);
      } else {
        this.standardLayers = List([this.backgroundLayer, this.poiLayer]);
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

  updateSize(): void {
    this.backgroundLayer.updateSize();
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
      if (mapMode === MapMode.surface) {
        newLayer = this.bitmapLayersSurface.get(networkType);
      } else if (mapMode === MapMode.survey) {
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
