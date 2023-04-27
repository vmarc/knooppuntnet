// this file is generated, please do not modify

import { TimeKey } from '@api/common';
import { Timestamp } from '@api/custom';

export interface ChangeKey {
  readonly replicationNumber: number;
  readonly timestamp: Timestamp;
  readonly changeSetId: number;
  readonly elementId: number;
  readonly time: TimeKey;
}
