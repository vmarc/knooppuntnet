// this file is generated, please do not modify

import { Tag } from '@api/custom/tag';
import { Timestamp } from '@api/custom/timestamp';

export interface Raw {
  readonly version: number;
  readonly changeSetId: number;
  readonly timestamp: Timestamp;
  readonly tags: Tag[];
}
