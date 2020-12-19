// this file is generated, please do not modify

import {BoundsI} from '../bounds-i';
import {LongdistanceRouteNokSegment} from './longdistance-route-nok-segment';
import {LongdistanceRouteSegment} from './longdistance-route-segment';

export interface LongdistanceRouteMapPage {
  readonly id: number;
  readonly ref: string;
  readonly name: string;
  readonly nameNl: string;
  readonly nameEn: string;
  readonly nameDe: string;
  readonly nameFr: string;
  readonly bounds: BoundsI;
  readonly gpxFilename: string;
  readonly osmSegments: LongdistanceRouteSegment[];
  readonly gpxGeometry: string;
  readonly okGeometry: string;
  readonly nokSegments: LongdistanceRouteNokSegment[];
}
