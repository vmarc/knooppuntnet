// this file is generated, please do not modify

import { RouteSegment } from '@api/common/route/route-segment';
import { SuperSegment } from '@api/common/route/super-segment';
import { MonitorRouteSummary } from './monitor-route-summary';

export interface MonitorRouteSegmentsPage {
  readonly summary: MonitorRouteSummary;
  readonly meters: number;
  readonly segments: RouteSegment[];
  readonly superDistance: number;
  readonly superSegments: SuperSegment[];
}
