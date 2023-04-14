// this file is generated, please do not modify

import { Change } from '@api/custom';
import { Timestamp } from '@api/custom';

export interface ChangeSet {
  readonly id: number;
  readonly timestamp: Timestamp;
  readonly timestampFrom: Timestamp;
  readonly timestampUntil: Timestamp;
  readonly timestampBefore: Timestamp;
  readonly timestampAfter: Timestamp;
  readonly changes: Change[];
}
