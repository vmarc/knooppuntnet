import { MonitorMapMode } from '@app/state/monitor/monitor-map-mode';

export interface MonitorMapState {
  mode: MonitorMapMode;
  routeIds: ReadonlyArray<string>;
  relationIds: ReadonlyArray<number>;
  deviationIds: ReadonlyArray<string>;
  routeEnabled: boolean;
  matchEnabled: boolean;
  deviationEnabled: boolean;
  monitorShowSegments: boolean;
}
