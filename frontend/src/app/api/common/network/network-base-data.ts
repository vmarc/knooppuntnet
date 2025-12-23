// this file is generated, please do not modify

import { RouteScope } from '@api/common/route-scope';
import { RouteType } from '@api/common/route-type';
import { Raw } from '@api/common/data/raw/raw';
import { RawMember } from '@api/common/data/raw/raw-member';

export interface NetworkBaseData {
  readonly raw: Raw;
  readonly name?: string;
  readonly routeType: RouteType;
  readonly routeScope: RouteScope;
  readonly members: RawMember[];
}
