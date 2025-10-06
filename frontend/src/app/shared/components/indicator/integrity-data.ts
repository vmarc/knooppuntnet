import { RouteScope } from '@api/common/route-scope';
import { RouteType } from '@api/common/route-type';

export interface IntegrityData {
  readonly routeType: RouteType;
  readonly routeScope: RouteScope;
  readonly actual: number;
  readonly expected: number;
}
