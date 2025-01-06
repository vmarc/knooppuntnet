// this file is generated, please do not modify

import { MemberType } from '@api/common/data';
import { Link } from './link';
import { RouteStructureRelation } from './route-structure-relation';
import { RouteStructureWay } from './route-structure-way';

export interface RouteStructureRow {
  readonly id: number;
  readonly memberType: MemberType;
  readonly role: string;
  readonly link?: Link;
  readonly distance: number;
  readonly name: string;
  readonly poi: string;
  readonly way?: RouteStructureWay;
  readonly relation?: RouteStructureRelation;
}
