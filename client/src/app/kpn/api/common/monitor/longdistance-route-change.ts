// this file is generated, please do not modify

import {BoundsI} from '../bounds-i';
import {ChangeKeyI} from '../changes/details/change-key-i';
import {LongdistanceRouteNokSegment} from './longdistance-route-nok-segment';
import {LongdistanceRouteSegment} from './longdistance-route-segment';

export interface LongdistanceRouteChange {
  readonly key: ChangeKeyI;
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
  readonly routeSegments: LongdistanceRouteSegment[];
  readonly newNokSegments: LongdistanceRouteNokSegment[];
  readonly resolvedNokSegments: LongdistanceRouteNokSegment[];
  readonly happy: boolean;
  readonly investigate: boolean;
}
