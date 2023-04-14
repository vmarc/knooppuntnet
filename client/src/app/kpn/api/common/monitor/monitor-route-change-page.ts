// this file is generated, please do not modify

import { Bounds } from '@api/common';
import { ChangeKey } from '@api/common/changes/details';
import { MonitorRouteDeviation } from './monitor-route-deviation';
import { MonitorRouteReferenceInfo } from './monitor-route-reference-info';
import { MonitorRouteSegment } from './monitor-route-segment';

export interface MonitorRouteChangePage {
  readonly key: ChangeKey;
  readonly groupName: string;
  readonly groupDescription: string;
  readonly comment: string;
  readonly wayCount: number;
  readonly waysAdded: number;
  readonly waysRemoved: number;
  readonly waysUpdated: number;
  readonly osmDistance: number;
  readonly bounds: Bounds;
  readonly routeSegmentCount: number;
  readonly routeSegments: MonitorRouteSegment[];
  readonly newDeviations: MonitorRouteDeviation[];
  readonly resolvedDeviations: MonitorRouteDeviation[];
  readonly reference: MonitorRouteReferenceInfo;
  readonly happy: boolean;
  readonly investigate: boolean;
}
