// this file is generated, please do not modify

import { Bounds } from '@api/common/bounds';
import { RouteScope } from '@api/common/route-scope';

export interface RouteSearchResult {
  readonly id: number;
  readonly name: string;
  readonly scopes: ReadonlyArray<RouteScope>;
  readonly distance: number;
  readonly symbol?: string;
  readonly bounds?: Bounds;
  readonly routeIds: ReadonlyArray<number>;
}
