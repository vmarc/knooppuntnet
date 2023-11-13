// this file is generated, please do not modify

import { Bounds } from '@api/common';
import { ChangeKey } from '@api/common/changes/details';
import { MonitorRouteDeviation } from './monitor-route-deviation';
import { MonitorRouteSegment } from './monitor-route-segment';

export interface MonitorRouteChangeDetail {
  readonly key: ChangeKey;
  readonly comment: string;
  readonly wayCount: number;
  readonly waysAdded: number;
  readonly waysRemoved: number;
  readonly waysUpdated: number;
  readonly osmDistance: number;
  readonly gpxDistance: number;
  readonly gpxFilename: string;
  readonly bounds: Bounds;
  readonly referenceJson: string;
  readonly routeSegmentCount: number;
  readonly routeSegments: MonitorRouteSegment[];
  readonly newDeviations: MonitorRouteDeviation[];
  readonly resolvedDeviations: MonitorRouteDeviation[];
  readonly happy: boolean;
  readonly investigate: boolean;
}
