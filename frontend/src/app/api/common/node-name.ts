// this file is generated, please do not modify

import { NetworkScope } from './network-scope';
import { RouteType } from './route-type';

export interface NodeName {
  readonly routeType: RouteType;
  readonly networkScope: NetworkScope;
  readonly name: string;
  readonly longName: string;
  readonly proposed: boolean;
}
