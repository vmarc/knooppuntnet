// this file is generated, please do not modify

import { Bounds } from '@api/common';
import { RouteScope } from '@api/common';

export interface RouteSearchResult {
  readonly id: number;
  readonly name: string;
  readonly scopes: RouteScope[];
  readonly distance: number;
  readonly symbol: string;
  readonly bounds: Bounds;
  readonly routeIds: number[];
}
