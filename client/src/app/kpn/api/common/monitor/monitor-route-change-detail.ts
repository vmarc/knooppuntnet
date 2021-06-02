// this file is generated, please do not modify

import { BoundsI } from '../bounds-i';
import { ChangeKey } from '../changes/details/change-key';
import { MonitorRouteNokSegment } from './monitor-route-nok-segment';
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
  readonly bounds: BoundsI;
  readonly referenceJson: string;
  readonly routeSegmentCount: number;
  readonly routeSegments: MonitorRouteSegment[];
  readonly newNokSegments: MonitorRouteNokSegment[];
  readonly resolvedNokSegments: MonitorRouteNokSegment[];
  readonly happy: boolean;
  readonly investigate: boolean;
}
