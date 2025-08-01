// this file is generated, please do not modify

import { Day } from '@api/custom/day';
import { BaseRouteSegment } from './base-route-segment';

export interface RouteStructureRelation {
  readonly physical: boolean;
  readonly name: string;
  readonly subRelationIndex: number;
  readonly survey?: Day;
  readonly symbol: string;
  readonly segments: BaseRouteSegment[];
  readonly totalDistance: number;
  readonly gaps: string;
  readonly happy: boolean;
}
