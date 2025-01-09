// this file is generated, please do not modify

import { RouteType } from '@api/common';
import { RoutePath } from './route-path';
import { RouteSegment } from './route-segment';

export interface RouteMapInfo {
  readonly routeId: number;
  readonly routeName: string;
  readonly routeType: RouteType;
  readonly segments: RouteSegment[];
  readonly paths: RoutePath[];
}
