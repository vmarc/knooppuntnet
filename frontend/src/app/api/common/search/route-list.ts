// this file is generated, please do not modify

import { RouteListItem } from './route-list-item';

export interface RouteList {
  readonly international?: RouteListItem[];
  readonly national?: RouteListItem[];
  readonly regional?: RouteListItem[];
  readonly local?: RouteListItem[];
  readonly unknown?: RouteListItem[];
  readonly size: number;
}
