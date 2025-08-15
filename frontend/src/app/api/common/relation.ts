// this file is generated, please do not modify

import { Member } from '@api/common/data/member';
import { Tag } from '@api/custom/tag';
import { Timestamp } from '@api/custom/timestamp';

export interface Relation {
  readonly id: number;
  readonly version: number;
  readonly timestamp: Timestamp;
  readonly changeSetId: number;
  readonly tags: Tag[];
  readonly members: Member[];
}
