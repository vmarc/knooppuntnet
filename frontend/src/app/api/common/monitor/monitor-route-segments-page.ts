// this file is generated, please do not modify

import { SegmentInfo } from '@api/common/route/segment-info';
import { MonitorRouteRelationInfo } from './monitor-route-relation-info';
import { MonitorRouteSummary } from './monitor-route-summary';

export interface MonitorRouteSegmentsPage {
  readonly summary: MonitorRouteSummary;
  readonly meters: number;
  readonly relations: MonitorRouteRelationInfo[];
  readonly segments: SegmentInfo[];
}
