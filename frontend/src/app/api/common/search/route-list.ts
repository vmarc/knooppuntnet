// this file is generated, please do not modify

import { RouteListItem } from './route-list-item';

export interface RouteList {
  readonly international?: ReadonlyArray<RouteListItem>;
  readonly national?: ReadonlyArray<RouteListItem>;
  readonly regional?: ReadonlyArray<RouteListItem>;
  readonly local?: ReadonlyArray<RouteListItem>;
  readonly unknown?: ReadonlyArray<RouteListItem>;
  readonly size: number;
}
