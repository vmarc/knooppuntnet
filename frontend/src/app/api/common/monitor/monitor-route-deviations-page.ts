// this file is generated, please do not modify

import { MonitorRouteDeviationInfo } from './monitor-route-deviation-info';
import { MonitorRouteSummary } from './monitor-route-summary';

export interface MonitorRouteDeviationsPage {
  readonly summary: MonitorRouteSummary;
  readonly deviationDistance: number;
  readonly deviations: ReadonlyArray<MonitorRouteDeviationInfo>;
}
