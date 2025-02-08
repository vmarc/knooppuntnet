// this file is generated, please do not modify

import { Tag } from '@api/custom/tag';
import { Timestamp } from '@api/custom/timestamp';

export interface RawNode {
  readonly id: number;
  readonly latitude: string;
  readonly longitude: string;
  readonly version: number;
  readonly timestamp: Timestamp;
  readonly changeSetId: number;
  readonly tags: Tag[];
}
