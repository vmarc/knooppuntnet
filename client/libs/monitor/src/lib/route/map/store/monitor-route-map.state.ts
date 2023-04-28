import { MonitorRouteDeviation } from '@api/common/monitor';
import { MonitorRouteMapPage } from '@api/common/monitor';
import { MonitorRouteSegment } from '@api/common/monitor';
import { MapPosition } from '@app/components/ol/domain';
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
  page: undefined,
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
  page: MonitorRouteMapPage | undefined;
  selectedDeviation: MonitorRouteDeviation | undefined;
  selectedOsmSegment: MonitorRouteSegment | undefined;
}

export const monitorRouteMapFeatureKey = 'monitor-route-map';
