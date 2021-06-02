// this file is generated, please do not modify

import { BoundsI } from '../bounds-i';
import { ChangeKey } from '../changes/details/change-key';

export interface LongdistanceRouteChangeSummary {
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
  readonly routeSegmentCount: number;
  readonly newNokSegmentCount: number;
  readonly resolvedNokSegmentCount: number;
  readonly happy: boolean;
  readonly investigate: boolean;
}
