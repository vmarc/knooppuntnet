// this file is generated, please do not modify

import { MonitorRouteChangeSummary } from './monitor-route-change-summary';

export interface MonitorChangesPage {
  readonly impact: boolean;
  readonly pageSize: number;
  readonly pageIndex: number;
  readonly totalChangeCount: number;
  readonly changes: MonitorRouteChangeSummary[];
}
