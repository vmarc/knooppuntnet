import { MonitorMapMode } from '@app/state/monitor/monitor-map-mode';

export interface MonitorMapState {
  mode: MonitorMapMode;
  routeIds: string[];
  relationIds: number[];
  deviationIds: string[];
  routeEnabled: boolean;
  matchEnabled: boolean;
  deviationEnabled: boolean;
  monitorShowSegments: boolean;
}
