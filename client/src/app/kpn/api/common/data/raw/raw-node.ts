// this file is generated, please do not modify

import { Tags } from '@api/custom/tags';
import { Timestamp } from '@api/custom/timestamp';

export interface RawNode {
  readonly id: number;
  readonly latitude: string;
  readonly longitude: string;
  readonly version: number;
  readonly timestamp: Timestamp;
  readonly changeSetId: number;
  readonly tags: Tags;
}
