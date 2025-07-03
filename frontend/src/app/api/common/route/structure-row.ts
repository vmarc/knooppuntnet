// this file is generated, please do not modify

import { MemberType } from '@api/common/data/member-type';
import { Day } from '@api/custom/day';
import { Timestamp } from '@api/custom/timestamp';
import { Link } from './link';
import { RouteStructureRelation } from './route-structure-relation';
import { RouteStructureWay } from './route-structure-way';

export interface StructureRow {
  readonly rowIndex: number;
  readonly level: number;
  readonly id: number;
  readonly memberType: MemberType;
  readonly role: string;
  readonly link?: Link;
  readonly distance: number;
  readonly name: string;
  readonly poi: string;
  readonly way?: RouteStructureWay;
  readonly relation?: RouteStructureRelation;
  readonly segmentIds: number[];
  readonly pathIds: number[];
  readonly physical: boolean;
  readonly relationId: number;
  readonly subRelationIndex: number;
  readonly survey?: Day;
  readonly symbol: string;
  readonly referenceTimestamp?: Timestamp;
  readonly referenceFilename: string;
  readonly referenceDistance: number;
  readonly deviationDistance: number;
  readonly deviationCount: number;
  readonly osmSegmentCount: number;
  readonly osmDistance: number;
  readonly osmDistanceSubRelations: number;
  readonly gaps: string;
  readonly showMap: boolean;
  readonly happy: boolean;
}
