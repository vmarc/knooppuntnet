import { Injectable } from '@angular/core';
import { NetworkType } from '@api/custom/network-type';
import { MapLayerState } from '@app/components/ol/domain/map-layer-state';
import { MapLayer } from '@app/components/ol/layers/map-layer';
import { NetworkBitmapTileLayer } from '@app/components/ol/layers/network-bitmap-tile-layer';
import { NetworkVectorTileLayer } from '@app/components/ol/layers/network-vector-tile-layer';
import { OpendataBitmapTileLayer } from '@app/components/ol/layers/opendata-bitmap-tile-layer';
import { OpendataVectorTileLayer } from '@app/components/ol/layers/opendata-vector-tile-layer';
import { OsmLayer } from '@app/components/ol/layers/osm-layer';
import { TileDebug256Layer } from '@app/components/ol/layers/tile-debug-256-layer';
import { TileDebug512Layer } from '@app/components/ol/layers/tile-debug-512-layer';
import { MapLayerService } from '@app/components/ol/services/map-layer.service';
import { MapMode } from '@app/components/ol/services/map-mode';
import { MapZoomService } from '@app/components/ol/services/map-zoom.service';
import { MapService } from '@app/components/ol/services/map.service';
import { PoiTileLayerService } from '@app/components/ol/services/poi-tile-layer.service';
import { SurveyDateValues } from '@app/components/ol/services/survey-date-values';
import { MainMapStyle } from '@app/components/ol/style/main-map-style';
import { MainMapStyleParameters } from '@app/components/ol/style/main-map-style-parameters';
import { selectPreferencesShowProposed } from '@app/core/preferences/preferences.selectors';
import { selectPreferencesExtraLayers } from '@app/core/preferences/preferences.selectors';
import { NetworkTypes } from '@app/kpn/common/network-types';
import { selectPlannerMapMode } from '@app/planner/store/planner-selectors';
import { selectPlannerMapPosition } from '@app/planner/store/planner-selectors';
import { Subscriptions } from '@app/util/Subscriptions';
import { Store } from '@ngrx/store';
import Map from 'ol/Map';
import { combineLatest } from 'rxjs';
import { first } from 'rxjs';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { PlannerService } from './planner.service';
import { BackgroundLayer } from '@app/components/ol/layers/background-layer';

@Injectable()
export class PlannerLayerService {
  public layers: MapLayer[];
  private parameters$: Observable<MainMapStyleParameters> = combineLatest([
    this.store.select(selectPlannerMapMode),
    this.store.select(selectPreferencesShowProposed),
    this.mapService.highlightedRouteId$,
  ]).pipe(
    map(([mapMode, showProposed, highlightedRouteId]) => {
      const surveyDateValues: SurveyDateValues = new SurveyDateValues(
        '',
        '',
        '',
        ''
      );
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
    this.buildLayers();
  }

  mapInit(olMap: Map) {
    this.store // no need to keep handle on subscription (will be closed after setting view position)
      .select(selectPlannerMapPosition)
      .pipe(first())
      .subscribe((mapPosition) => {
        olMap.getView().setZoom(mapPosition.zoom);
        olMap.getView().setCenter([mapPosition.x, mapPosition.y]);
      });

    // List(this.vectorLayers.values()).forEach((mapLayer) => {
    //   const vectorTileLayer = mapLayer.layer as VectorTileLayer;
    //   vectorTileLayer.setStyle(mainMapStyle);
    //   this.mapRelatedSubscriptions.add(
    //     this.mapService.mapMode$.subscribe(() =>
    //       vectorTileLayer.getSource().changed()
    //     ),
    //     this.store
    //       .select(selectPreferencesShowProposed)
    //       .subscribe(() => vectorTileLayer.getSource().changed())
    //   );
    // });
  }

  mapDestroy(olMap: Map): void {
    this.mapRelatedSubscriptions.unsubscribe();
    this.networkVectorLayerStyle.destroy();
  }

  private buildLayers(): void {
    this.layers = [];

    this.layers.push(new OsmLayer().build());
    this.layers.push(new BackgroundLayer().build());
    this.layers.push(new TileDebug256Layer().build());
    this.layers.push(new TileDebug512Layer().build());
    this.layers.push(this.poiTileLayerService.buildLayer());

    this.layers.push(
      new OpendataBitmapTileLayer().build(
        NetworkType.hiking,
        'flanders-hiking',
        'flanders/hiking'
      )
    );

    this.layers.push(
      new OpendataVectorTileLayer().build(
        NetworkType.hiking,
        'flanders-hiking',
        'flanders/hiking'
      )
    );

    /* TODO planner this.allLayers.push(new OpendataBitmapTileLayer().build(NetworkType.cycling)); */
    /* TODO planner this.allLayers.push(new OpendataVectorTileLayer().build(NetworkType.cycling)); */

    this.layers.push(
      new OpendataBitmapTileLayer().build(
        NetworkType.hiking,
        'netherlands-hiking',
        'netherlands/hiking'
      )
    );

    this.layers.push(
      new OpendataVectorTileLayer().build(
        NetworkType.hiking,
        'netherlands-hiking',
        'netherlands/hiking'
      )
    );

    NetworkTypes.all.forEach((networkType) => {
      this.layers.push(NetworkBitmapTileLayer.build(networkType, 'surface'));
      this.layers.push(NetworkBitmapTileLayer.build(networkType, 'survey'));
      this.layers.push(NetworkBitmapTileLayer.build(networkType, 'analysis'));
      this.layers.push(
        NetworkVectorTileLayer.build(
          networkType,
          this.networkVectorLayerStyle.styleFunction()
        )
      );
    });

    this.layers.forEach((layer) => layer.layer.setVisible(false));
  }

  updateLayerVisibility(
    layerStates: MapLayerState[],
    networkType: NetworkType,
    mapMode: MapMode,
    zoom: number,
    pois: boolean
  ): void {
    console.log(
      `updateLayerVisibility(zoom=${zoom}, networkType=${networkType}, mapMode=${mapMode})`
    );
    this.layers.forEach((mapLayer) => {
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

    const visibleLayers = this.layers
      .filter((mapLayer) => mapLayer.layer.getVisible())
      .map(
        (mapLayer) =>
          `${mapLayer.name}/${mapLayer.id}(minZoom=${mapLayer.minZoom}, maxZoom=${mapLayer.maxZoom})`
      )
      .join(',');
    console.log(`visibleLayers=${visibleLayers}`);
  }

  updateSize(): void {
    // this.backgroundLayer.updateSize();
  }
}
