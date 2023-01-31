import { MonitorRouteDeviation } from '@api/common/monitor/monitor-route-deviation';
import { MonitorRouteMapPage } from '@api/common/monitor/monitor-route-map-page';
import { MonitorRouteSegment } from '@api/common/monitor/monitor-route-segment';
import { ApiResponse } from '@api/custom/api-response';
import { MapPosition } from '../../../../components/ol/domain/map-position';
import { MonitorMapMode } from '../monitor-map-mode';

export const initialState: MonitorRouteMapState = {
  mapMode: undefined,
  mapReferenceVisible: undefined,
  mapMatchesVisible: undefined,
  mapDeviationsVisible: undefined,
  mapOsmRelationVisible: undefined,
  mapOsmRelationAvailable: undefined,
  mapOsmRelationEmpty: undefined,
  mapPosition: undefined,
  mapPages: undefined,
  routeMapPage: undefined,
  routeMapSelectedDeviation: undefined,
  routeMapSelectedOsmSegment: undefined,
};

export interface MonitorRouteMapState {
  mapMode: MonitorMapMode | undefined;
  mapReferenceVisible: boolean | undefined;
  mapMatchesVisible: boolean | undefined;
  mapDeviationsVisible: boolean | undefined;
  mapOsmRelationVisible: boolean | undefined;
  mapOsmRelationAvailable: boolean | undefined;
  mapOsmRelationEmpty: boolean | undefined;
  mapPosition: MapPosition | undefined;
  mapPages: Map<number, MonitorRouteMapPage> | undefined;
  routeMapPage: ApiResponse<MonitorRouteMapPage> | undefined;
  routeMapSelectedDeviation: MonitorRouteDeviation | undefined;
  routeMapSelectedOsmSegment: MonitorRouteSegment | undefined;
}

export const monitorRouteMapFeatureKey = 'monitor-route-map';
