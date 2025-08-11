// this file is generated, please do not modify

import { RouteScope } from './route-scope';
import { RouteType } from './route-type';

export interface NodeName {
  readonly routeType: RouteType;
  readonly routeScope: RouteScope;
  readonly name: string;
  readonly longName?: string;
  readonly proposed: boolean;
}
