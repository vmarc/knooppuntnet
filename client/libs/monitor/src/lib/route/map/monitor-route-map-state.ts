import { MonitorRouteSegment } from '@api/common/monitor';
import { MonitorRouteDeviation } from '@api/common/monitor';
import { MonitorRouteMapPage } from '@api/common/monitor';
import { MonitorMapMode } from './monitor-map-mode';

export const initialState: MonitorRouteMapState = {
  page: null,
  mode: MonitorMapMode.comparison,
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
  readonly mode: MonitorMapMode;
  readonly referenceVisible: boolean;
  readonly matchesVisible: boolean;
  readonly deviationsVisible: boolean;
  readonly osmRelationVisible: boolean;
  readonly selectedDeviation: MonitorRouteDeviation | null;
  readonly selectedOsmSegment: MonitorRouteSegment | null;
  readonly referenceAvailable: boolean;
}
