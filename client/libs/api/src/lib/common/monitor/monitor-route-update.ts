// this file is generated, please do not modify

import { Timestamp } from '@api/custom';

export interface MonitorRouteUpdate {
  readonly action: string;
  readonly groupName: string;
  readonly routeName: string;
  readonly referenceType: string;
  readonly description: string;
  readonly comment: string;
  readonly relationId: number;
  readonly referenceTimestamp: Timestamp;
  readonly referenceFilename: string;
  readonly referenceGpx: string;
  readonly newGroupName: string;
  readonly newRouteName: string;
}
