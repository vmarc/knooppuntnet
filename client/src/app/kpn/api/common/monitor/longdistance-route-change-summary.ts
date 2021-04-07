// this file is generated, please do not modify

import { BoundsI } from '../bounds-i';
import { ChangeKeyI } from '../changes/details/change-key-i';

export interface LongdistanceRouteChangeSummary {
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
  readonly routeSegmentCount: number;
  readonly newNokSegmentCount: number;
  readonly resolvedNokSegmentCount: number;
  readonly happy: boolean;
  readonly investigate: boolean;
}
