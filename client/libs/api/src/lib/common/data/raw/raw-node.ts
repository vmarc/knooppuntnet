// this file is generated, please do not modify

import { Tags } from '@api/custom';
import { Timestamp } from '@api/custom';

export interface RawNode {
  readonly id: number;
  readonly latitude: string;
  readonly longitude: string;
  readonly version: number;
  readonly timestamp: Timestamp;
  readonly changeSetId: number;
  readonly tags: Tags;
}
