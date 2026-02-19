// this file is generated, please do not modify

import { Bounds } from '@api/common/bounds';
import { ChangeKey } from '@api/common/changes/details/change-key';
import { MonitorReferenceInfo } from './monitor-reference-info';
import { MonitorRouteDeviation } from './monitor-route-deviation';
import { MonitorRouteSegment } from './monitor-route-segment';

export interface MonitorRouteChangePage {
  readonly key: ChangeKey;
  readonly groupName: string;
  readonly groupDescription: string;
  readonly comment?: string;
  readonly wayCount: number;
  readonly waysAdded: number;
  readonly waysRemoved: number;
  readonly waysUpdated: number;
  readonly osmDistance: number;
  readonly bounds: Bounds;
  readonly routeSegmentCount: number;
  readonly routeSegments: ReadonlyArray<MonitorRouteSegment>;
  readonly newDeviations: ReadonlyArray<MonitorRouteDeviation>;
  readonly resolvedDeviations: ReadonlyArray<MonitorRouteDeviation>;
  readonly reference: MonitorReferenceInfo;
  readonly happy: boolean;
  readonly investigate: boolean;
}
