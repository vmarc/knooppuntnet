// this file is generated, please do not modify

import { Bounds } from '@api/common';

export interface RouteListItem {
  readonly id: number;
  readonly name: string;
  readonly distance: number;
  readonly symbol: string;
  readonly bounds?: Bounds;
  readonly routeIds: number[];
}
