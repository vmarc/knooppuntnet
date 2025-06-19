// this file is generated, please do not modify

import { Bounds } from '@api/common/bounds';
import { RouteType } from '@api/common/route-type';
import { RoutePath } from './route-path';

export interface RoutePathData {
  readonly name: string;
  readonly routeTypes: RouteType[];
  readonly paths: RoutePath[];
  readonly bounds?: Bounds;
}
