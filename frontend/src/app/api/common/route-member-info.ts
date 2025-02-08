// this file is generated, please do not modify

import { MemberType } from '@api/common/data/member-type';
import { RouteMemberInfoWay } from './route-member-info-way';

export interface RouteMemberInfo {
  readonly id: number;
  readonly memberType: MemberType;
  readonly role: string;
  readonly name: string;
  readonly poi: string;
  readonly way?: RouteMemberInfoWay;
}
