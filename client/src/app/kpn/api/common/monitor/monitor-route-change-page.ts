// this file is generated, please do not modify

import { BoundsI } from '../bounds-i';
import { ChangeKey } from '../changes/details/change-key';
import { MonitorRouteNokSegment } from './monitor-route-nok-segment';
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
  readonly bounds: BoundsI;
  readonly routeSegmentCount: number;
  readonly routeSegments: MonitorRouteSegment[];
  readonly newNokSegments: MonitorRouteNokSegment[];
  readonly resolvedNokSegments: MonitorRouteNokSegment[];
  readonly reference: MonitorRouteReferenceInfo;
  readonly happy: boolean;
  readonly investigate: boolean;
}
