// this file is generated, please do not modify

import { Day } from '@api/custom/day';

export interface RouteStructureRelation {
  readonly level: number;
  readonly physical: boolean;
  readonly name: string;
  readonly subRelationIndex: number;
  readonly survey?: Day;
  readonly symbol: string;
  readonly osmSegmentCount: number;
  readonly totalDistance: number;
  readonly gaps: string;
  readonly happy: boolean;
}
