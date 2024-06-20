// this file is generated, please do not modify

import { Tag } from '@api/custom';
import { Timestamp } from '@api/custom';

export interface RawWay {
  readonly id: number;
  readonly version: number;
  readonly timestamp: Timestamp;
  readonly changeSetId: number;
  readonly nodeIds: number[];
  readonly tags: Tag[];
}
