// this file is generated, please do not modify

import { Country } from '@api/common/country';
import { RouteType } from '@api/common/route-type';

export interface SubsetInfo {
  readonly country: Country;
  readonly routeType: RouteType;
  readonly networkCount: number;
  readonly factCount: number;
  readonly changesCount: number;
  readonly orphanNodeCount: number;
  readonly orphanRouteCount: number;
}
