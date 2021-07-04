// this file is generated, please do not modify

import { BoundsI } from '../bounds-i';
import { ChangeKeyI } from '../changes/details/change-key-i';
import { MonitorRouteNokSegment } from './monitor-route-nok-segment';
import { MonitorRouteSegment } from './monitor-route-segment';

export interface MonitorRouteChange {
  readonly key: ChangeKeyI;
  readonly comment: string;
  readonly wayCount: number;
  readonly waysAdded: number;
  readonly waysRemoved: number;
  readonly waysUpdated: number;
  readonly osmDistance: number;
  readonly routeSegmentCount: number;
  readonly routeSegments: MonitorRouteSegment[];
  readonly newNokSegments: MonitorRouteNokSegment[];
  readonly resolvedNokSegments: MonitorRouteNokSegment[];
  readonly happy: boolean;
  readonly investigate: boolean;
}
