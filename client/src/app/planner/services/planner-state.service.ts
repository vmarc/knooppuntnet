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
import { NetworkTypes } from '@app/kpn/common/network-types';

@Injectable({
  providedIn: 'root',
})
export class PlannerStateService {
  readonly plannerPositionKey = 'planner-position';

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

  constructor(private browserStorageService: BrowserStorageService) {}

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

  toPlannerState(routeParams: Params, queryParams: Params): PlannerState {
    const networkType = this.parseNetworkType(routeParams);
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

  public parseNetworkType(queryParams: Params): NetworkType {
    const networkTypeParam = queryParams['networkType'];
    if (!!networkTypeParam) {
      const exists = NetworkTypes.all.some(
        (networkType) => networkType === networkTypeParam
      );
      if (exists) {
        return NetworkType[networkTypeParam];
      }
    }
    return NetworkType.hiking;
  }

  public parsePosition(queryParams: Params): MapPosition {
    const positionParam = queryParams['position'];
    let position = MapPosition.fromQueryParam(positionParam);
    console.log(`  position=${position}`);
    if (!position) {
      const mapPositionString = this.browserStorageService.get(
        this.plannerPositionKey
      );
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

  public parseMapMode(queryParams: Params): MapMode {
    const mapModeParam = queryParams['mode'];
    let mapMode: MapMode = 'surface';
    if (mapModeParam === 'survey') {
      mapMode = 'survey';
    } else if (mapModeParam === 'analysis') {
      mapMode = 'analysis';
    }
    return mapMode;
  }

  public parseResultMode(queryParams: Params): string {
    const resultModeParam = queryParams['result'];
    let resultMode = 'compact';
    if (resultModeParam === 'details') {
      resultMode = 'details';
    }
    return resultMode;
  }

  public parsePoisFlag(queryParams: Params): boolean {
    const poisParam = queryParams['pois'];
    return !!poisParam ? poisParam === 'true' : false;
  }

  public parsePoiLayers(queryParams: Params): MapLayerState[] {
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
