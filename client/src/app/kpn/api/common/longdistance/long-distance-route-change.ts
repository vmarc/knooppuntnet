// this class is generated, please do not modify

import {BoundsI} from '../bounds-i';
import {ChangeKeyI} from '../changes/details/change-key-i';
import {LongDistanceRouteNokSegment} from './long-distance-route-nok-segment';
import {LongDistanceRouteSegment} from './long-distance-route-segment';

export interface LongDistanceRouteChange {
  readonly key: ChangeKeyI;
  readonly wayCount: number;
  readonly waysAdded: number;
  readonly waysRemoved: number;
  readonly waysUpdated: number;
  readonly osmDistance: number;
  readonly gpxDistance: number;
  readonly gpxFilename: string;
  readonly bounds: BoundsI;
  readonly referenceJson: string;
  readonly routeSegments: LongDistanceRouteSegment[];
  readonly newNokSegments: LongDistanceRouteNokSegment[];
  readonly resolvedNokSegments: LongDistanceRouteNokSegment[];
  readonly happy: boolean;
  readonly investigate: boolean;
}
