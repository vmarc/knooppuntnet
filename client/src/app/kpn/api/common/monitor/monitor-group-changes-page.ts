// this file is generated, please do not modify

import { MonitorRouteChangeSummary } from './monitor-route-change-summary';

export interface MonitorGroupChangesPage {
  readonly groupName: string;
  readonly groupDescription: string;
  readonly impact: boolean;
  readonly pageIndex: number;
  readonly itemsPerPage: number;
  readonly totalChangeCount: number;
  readonly changes: MonitorRouteChangeSummary[];
}
