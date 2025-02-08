// this file is generated, please do not modify

import { Country } from '@api/common/country';
import { LatLonImpl } from '@api/common/lat-lon-impl';
import { RouteScope } from '@api/common/route-scope';
import { RouteType } from '@api/common/route-type';
import { Timestamp } from '@api/custom/timestamp';
import { Integrity } from './integrity';

export interface NetworkAttributes {
  readonly id: number;
  readonly country?: Country;
  readonly routeType: RouteType;
  readonly routeScope: RouteScope;
  readonly name: string;
  readonly km: number;
  readonly meters: number;
  readonly nodeCount: number;
  readonly routeCount: number;
  readonly brokenRouteCount: number;
  readonly brokenRoutePercentage: string;
  readonly integrity: Integrity;
  readonly inaccessibleRouteCount: number;
  readonly connectionCount: number;
  readonly lastUpdated: Timestamp;
  readonly relationLastUpdated: Timestamp;
  readonly center?: LatLonImpl;
}
