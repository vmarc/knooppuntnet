// this file is generated, please do not modify

import { LocationChangesTreeNode } from './location-changes-tree-node';
import { RouteType } from './route-type';

export interface LocationChangesTree {
  readonly routeType: RouteType;
  readonly locationName: string;
  readonly happy: boolean;
  readonly investigate: boolean;
  readonly children: LocationChangesTreeNode[];
}
