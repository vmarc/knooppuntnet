// this file is generated, please do not modify

import { Timestamp } from '@api/custom/timestamp';
import { MonitorReferenceType } from './monitor-reference-type';

export interface MonitorRouteProperties {
  readonly groupName: string;
  readonly name: string;
  readonly description: string;
  readonly comment?: string;
  readonly relationId?: number;
  readonly referenceType: MonitorReferenceType;
  readonly referenceTimestamp?: Timestamp;
  readonly referenceFilename?: string;
  readonly referenceFileChanged: boolean;
}
