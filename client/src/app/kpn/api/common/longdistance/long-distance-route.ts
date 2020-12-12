// this class is generated, please do not modify

// this class is generated, please do not modify

import {BoundsI} from '../bounds-i';
import {LongDistanceRouteNokSegment} from './long-distance-route-nok-segment';
import {LongDistanceRouteSegment} from './long-distance-route-segment';

export interface LongDistanceRoute {
  readonly id: number;
  readonly ref: string;
  readonly name: string;
  readonly nameNl: string;
  readonly nameEn: string;
  readonly nameDe: string;
  readonly nameFr: string;
  readonly description: string;
  readonly operator: string;
  readonly website: string;
  readonly wayCount: number;
  readonly osmDistance: number;
  readonly gpxDistance: number;
  readonly bounds: BoundsI;
  readonly gpxFilename: string;
  readonly osmSegments: LongDistanceRouteSegment[];
  readonly gpxGeometry: string;
  readonly okGeometry: string;
  readonly nokSegments: LongDistanceRouteNokSegment[];
}
