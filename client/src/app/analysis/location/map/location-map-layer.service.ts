import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { Observable } from 'rxjs';
import { of } from 'rxjs';
import { MapLayer } from '@app/components/ol/layers/map-layer';
import { MapLayerState } from '@app/components/ol/domain/map-layer-state';
import { BackgroundLayer } from '@app/components/ol/layers/background-layer';
import { OsmLayer } from '@app/components/ol/layers/osm-layer';
import { NetworkVectorTileLayer } from '@app/components/ol/layers/network-vector-tile-layer';
import { MainMapStyleParameters } from '@app/components/ol/style/main-map-style-parameters';
import { MainMapStyle } from '@app/components/ol/style/main-map-style';
import { NetworkBitmapTileLayer } from '@app/components/ol/layers/network-bitmap-tile-layer';
import { LocationBoundaryLayer } from '@app/components/ol/layers/location-boundary-layer';
import { SurveyDateValues } from '@app/components/ol/services/survey-date-values';
import { NetworkType } from '@api/custom/network-type';
import { MapPosition } from '@app/components/ol/domain/map-position';

@Injectable()
export class LocationMapLayerService {
  private _layers$ = new BehaviorSubject<MapLayer[]>([]);
  public layers$: Observable<MapLayer[]> = this._layers$.asObservable();

  buildLayers(
    networkType: NetworkType,
    surveyDateValues: SurveyDateValues,
    geoJson: string
  ): MapLayerState[] {
    const layers: MapLayer[] = [];
    const layerStates: MapLayerState[] = [];

    const parameters$: Observable<MainMapStyleParameters> = of(
      new MainMapStyleParameters(
        'analysis',
        true,
        surveyDateValues,
        null,
        null,
        null
      )
    );
    const mainMapStyle = new MainMapStyle(parameters$);

    layers.push(BackgroundLayer.build());
    layerStates.push({ layerName: BackgroundLayer.id, visible: true });

    layers.push(OsmLayer.build());
    layerStates.push({ layerName: 'osm', visible: false });

    layers.push(
      NetworkVectorTileLayer.build(networkType, mainMapStyle.styleFunction())
    );
    layers.push(NetworkBitmapTileLayer.build(networkType, 'analysis'));
    layerStates.push({ layerName: networkType, visible: true });

    layers.push(LocationBoundaryLayer.build(geoJson));
    layerStates.push({ layerName: LocationBoundaryLayer.id, visible: true });

    this._layers$.next(layers);
    return layerStates;
  }

  updateLayerVisibility(
    layerStates: MapLayerState[],
    mapPosition: MapPosition
  ): void {
    this._layers$.value.forEach((mapLayer) => {
      const mapLayerState = layerStates.find(
        (layerState) => layerState.layerName === mapLayer.name
      );
      const zoom = mapPosition.zoom;
      const visible =
        mapLayerState &&
        mapLayerState.visible &&
        zoom >= mapLayer.minZoom &&
        zoom <= mapLayer.maxZoom;
      if (
        visible &&
        mapLayer.id.includes('vector') &&
        mapLayer.layer.getVisible()
      ) {
        mapLayer.layer.changed();
      }
      mapLayer.layer.setVisible(visible);
    });
  }
}
