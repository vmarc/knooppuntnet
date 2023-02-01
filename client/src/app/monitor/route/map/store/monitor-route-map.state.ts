import { MonitorRouteDeviation } from '@api/common/monitor/monitor-route-deviation';
import { MonitorRouteMapPage } from '@api/common/monitor/monitor-route-map-page';
import { MonitorRouteSegment } from '@api/common/monitor/monitor-route-segment';
import { ApiResponse } from '@api/custom/api-response';
import { MapPosition } from '../../../../components/ol/domain/map-position';
import { MonitorMapMode } from '../monitor-map-mode';

export const initialState: MonitorRouteMapState = {
  mode: undefined,
  referenceVisible: undefined,
  matchesVisible: undefined,
  deviationsVisible: undefined,
  osmRelationVisible: undefined,
  osmRelationAvailable: undefined,
  osmRelationEmpty: undefined,
  mapPosition: undefined,
  pages: undefined,
  pageResponse: undefined,
  selectedDeviation: undefined,
  selectedOsmSegment: undefined,
};

export interface MonitorRouteMapState {
  mode: MonitorMapMode | undefined;
  referenceVisible: boolean | undefined;
  matchesVisible: boolean | undefined;
  deviationsVisible: boolean | undefined;
  osmRelationVisible: boolean | undefined;
  osmRelationAvailable: boolean | undefined;
  osmRelationEmpty: boolean | undefined;
  mapPosition: MapPosition | undefined;
  pages: Map<number, MonitorRouteMapPage> | undefined;
  pageResponse: ApiResponse<MonitorRouteMapPage> | undefined;
  selectedDeviation: MonitorRouteDeviation | undefined;
  selectedOsmSegment: MonitorRouteSegment | undefined;
}

export const monitorRouteMapFeatureKey = 'monitor-route-map';
