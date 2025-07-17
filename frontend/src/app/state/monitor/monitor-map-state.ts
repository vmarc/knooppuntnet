import { MonitorMapMode } from '@app/state/monitor/monitor-map-mode';

export interface MonitorMapState {
  mode: MonitorMapMode;
  routeIds: string[];
  deviationIds: string[];
  referenceEnabled: boolean;
  matchEnabled: boolean;
  deviationEnabled: boolean;
}
