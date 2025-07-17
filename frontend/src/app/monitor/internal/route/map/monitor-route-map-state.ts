import { MonitorRouteSegment } from '@api/common/monitor/monitor-route-segment';
import { MonitorRouteDeviation } from '@api/common/monitor/monitor-route-deviation';
import { MonitorRouteMapPage } from '@api/common/monitor/monitor-route-map-page';
import { OldMonitorMapMode } from './old-monitor-map-mode';

export const initialState: MonitorRouteMapState = {
  page: null,
  mode: OldMonitorMapMode.comparison,
  referenceVisible: false,
  matchesVisible: false,
  deviationsVisible: false,
  osmRelationVisible: false,
  selectedDeviation: null,
  selectedOsmSegment: null,
  referenceAvailable: false,
};

export interface MonitorRouteMapState {
  readonly page: MonitorRouteMapPage | null;
  readonly mode: OldMonitorMapMode;
  readonly referenceVisible: boolean;
  readonly matchesVisible: boolean;
  readonly deviationsVisible: boolean;
  readonly osmRelationVisible: boolean;
  readonly selectedDeviation: MonitorRouteDeviation | null;
  readonly selectedOsmSegment: MonitorRouteSegment | null;
  readonly referenceAvailable: boolean;
}
