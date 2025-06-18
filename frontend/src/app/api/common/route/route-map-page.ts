// this file is generated, please do not modify

import { Bounds } from '@api/common/bounds';
import { RouteInfo } from './route-info';
import { RouteMapInfo } from './route-map-info';

export interface RouteMapPage {
  readonly routeInfo: RouteInfo;
  readonly routeMapInfo: RouteMapInfo;
  readonly bounds: Bounds;
}
