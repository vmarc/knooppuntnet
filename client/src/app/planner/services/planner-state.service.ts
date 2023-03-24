import { Injectable } from '@angular/core';
import { Params } from '@angular/router';
import { NetworkType } from '@api/custom/network-type';
import { MapLayerState } from '@app/components/ol/domain/map-layer-state';
import { MapPosition } from '@app/components/ol/domain/map-position';
import { MapMode } from '@app/components/ol/services/map-mode';
import { PlannerState } from '@app/planner/store/planner-state';
import { BrowserStorageService } from '@app/services/browser-storage.service';
import { Coordinate } from 'ol/coordinate';
import { fromLonLat } from 'ol/proj';

@Injectable({
  providedIn: 'root',
})
export class PlannerStateService {
  readonly mapPositionKey = 'map-position';

  private readonly defaultPoiLayerStates: MapLayerState[] = [
    { layerName: 'hiking-biking', visible: true },
    { layerName: 'landmarks', visible: true },
    { layerName: 'restaurants', visible: true },
    { layerName: 'places-to-stay', visible: true },
    { layerName: 'tourism', visible: true },
    { layerName: 'amenity', visible: false },
    { layerName: 'shops', visible: false },
    { layerName: 'foodshops', visible: false },
    { layerName: 'sports', visible: false },
  ];

  constructor(private storage: BrowserStorageService) {}

  toQueryParams(state: PlannerState): Params {
    const position = state.position.toQueryParam();
    const mode = state.mapMode;
    const result = state.resultMode;
    const layers = state.layerStates
      .filter((ls) => ls.visible)
      .map((ls) => ls.layerName)
      .join(',');
    const pois = state.pois.toString();
    const poiLayers = state.poiLayerStates
      .filter((ls) => ls.visible)
      .map((ls) => ls.layerName)
      .join(',');

    return {
      mode,
      position,
      result,
      layers,
      pois,
      'poi-layers': poiLayers,
    };
  }

  toPlannerState(queryParams: Params): PlannerState {
    const networkType = this.parseNetworkType(queryParams);
    const position = this.parsePosition(queryParams);
    const mapMode = this.parseMapMode(queryParams);
    const resultMode = this.parseResultMode(queryParams);
    const layerStates: MapLayerState[] = []; // TODO planner --> read layers from query param!!
    const pois = this.parsePoisFlag(queryParams);
    const poiLayerStates = this.parsePoiLayers(queryParams);

    return {
      networkType,
      position,
      mapMode,
      resultMode,
      layerStates,
      pois,
      poiLayerStates,
    };
  }

  private parseNetworkType(queryParams: Params): NetworkType {
    const networkTypeParam = queryParams['networkType'];
    return !!networkTypeParam
      ? NetworkType[networkTypeParam]
      : NetworkType.hiking;
  }

  private parsePosition(queryParams: Params): MapPosition {
    const positionParam = queryParams['position'];
    let position = MapPosition.fromQueryParam(positionParam);
    console.log(`  position=${position}`);
    if (!position) {
      const mapPositionString = this.storage.get(this.mapPositionKey);
      console.log(`  mapPositionString=${mapPositionString}`);
      if (!!mapPositionString) {
        position = MapPosition.fromQueryParam(mapPositionString);
        console.log(
          `initial position from lcoal storage: ${position.toQueryParam()}`
        );
      } else {
        // TODO replace temporary code
        const a: Coordinate = fromLonLat([2.24, 50.16]);
        const b: Coordinate = fromLonLat([10.56, 54.09]);
        // const extent: Extent = [a[0], a[1], b[0], b[1]];
        const x = (b[0] - a[0]) / 2 + a[0];
        const y = (b[1] - a[1]) / 2 + a[1];
        position = new MapPosition(14, x, y, 0);
        console.log(
          `initial position from temporary code: ${position.toQueryParam()}`
        );
      }
    } else {
      console.log(
        `initial position from query params: ${position.toQueryParam()}`
      );
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

  private parsePoisFlag(queryParams: Params): boolean {
    const poisParam = queryParams['pois'];
    return !!poisParam ? poisParam === 'true' : false;
  }

  private parsePoiLayers(queryParams: Params): MapLayerState[] {
    const poiLayersParam = queryParams['poi-layers'];
    let poiLayerStates: MapLayerState[];
    if (!!poiLayersParam) {
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
