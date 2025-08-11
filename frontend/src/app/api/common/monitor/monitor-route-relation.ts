// this file is generated, please do not modify

import { Day } from '@api/custom/day';
import { Timestamp } from '@api/custom/timestamp';

export interface MonitorRouteRelation {
  readonly relationId: number;
  readonly name: string;
  readonly role?: string;
  readonly survey?: Day;
  readonly symbol?: string;
  readonly referenceTimestamp?: Timestamp;
  readonly referenceFilename?: string;
  readonly referenceDistance: number;
  readonly deviationDistance: number;
  readonly deviationCount: number;
  readonly happy: boolean;
  readonly relations: MonitorRouteRelation[];
}
