// this file is generated, please do not modify

import { Bounds } from '@api/common/bounds';
import { MonitorRouteDetail } from './monitor-route-detail';

export interface MonitorGroupPage {
  readonly groupId: string;
  readonly groupName: string;
  readonly groupDescription: string;
  readonly adminRole: boolean;
  readonly bounds?: Bounds;
  readonly routes: MonitorRouteDetail[];
}
