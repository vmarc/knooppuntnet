// this file is generated, please do not modify

import { Bounds } from '@api/common/bounds';
import { MonitorRouteDetail } from './monitor-route-detail';

export interface MonitorGroupPage {
  readonly adminUser: boolean;
  readonly groupId: string;
  readonly groupName: string;
  readonly groupDescription: string;
  readonly bounds?: Bounds;
  readonly relationIds: ReadonlyArray<number>;
  readonly routes: ReadonlyArray<MonitorRouteDetail>;
}
