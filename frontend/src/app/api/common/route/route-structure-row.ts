// this file is generated, please do not modify

import { MemberType } from '@api/common/data';
import { RouteStructureRelation } from './route-structure-relation';
import { RouteStructureWay } from './route-structure-way';

export interface RouteStructureRow {
  readonly id: number;
  readonly memberType: MemberType;
  readonly role: string;
  readonly linkName: string;
  readonly distance: number;
  readonly way: RouteStructureWay;
  readonly relation: RouteStructureRelation;
}
