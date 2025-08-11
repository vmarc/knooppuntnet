// this file is generated, please do not modify

import { Timestamp } from '@api/custom/timestamp';
import { MonitorAction } from './monitor-action';
import { MonitorReferenceType } from './monitor-reference-type';

export interface MonitorRouteUpdate {
  readonly action: MonitorAction;
  readonly groupName: string;
  readonly routeName: string;
  readonly referenceType: MonitorReferenceType;
  readonly description?: string;
  readonly comment?: string;
  readonly relationId?: number;
  readonly referenceTimestamp?: Timestamp;
  readonly referenceFilename?: string;
  readonly referenceGpx?: string;
  readonly migrationGeojson?: string;
  readonly newGroupName?: string;
  readonly newRouteName?: string;
}
