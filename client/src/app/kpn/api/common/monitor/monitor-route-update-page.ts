// this file is generated, please do not modify

import { MonitorRouteGroup } from './monitor-route-group';
import { MonitorRouteProperties } from './monitor-route-properties';

export interface MonitorRouteUpdatePage {
  readonly groupName: string;
  readonly groupDescription: string;
  readonly routeName: string;
  readonly routeDescription: string;
  readonly groups: MonitorRouteGroup[];
  readonly properties: MonitorRouteProperties;
}
