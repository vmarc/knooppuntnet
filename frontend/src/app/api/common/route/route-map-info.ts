// this file is generated, please do not modify

import { NetworkType } from '@api/common';
import { RoutePath } from './route-path';
import { RouteSegment } from './route-segment';

export interface RouteMapInfo {
  readonly routeId: number;
  readonly routeName: string;
  readonly networkType: NetworkType;
  readonly segments: RouteSegment[];
  readonly paths: RoutePath[];
}
