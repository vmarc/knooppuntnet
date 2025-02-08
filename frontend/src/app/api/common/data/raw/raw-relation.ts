// this file is generated, please do not modify

import { Tag } from '@api/custom/tag';
import { Timestamp } from '@api/custom/timestamp';
import { RawMember } from './raw-member';

export interface RawRelation {
  readonly id: number;
  readonly version: number;
  readonly timestamp: Timestamp;
  readonly changeSetId: number;
  readonly members: RawMember[];
  readonly tags: Tag[];
}
