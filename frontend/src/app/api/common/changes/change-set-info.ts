// this file is generated, please do not modify

import { Tag } from '@api/custom/tag';
import { Timestamp } from '@api/custom/timestamp';

export interface ChangeSetInfo {
  readonly _id: number;
  readonly id: number;
  readonly createdAt: Timestamp;
  readonly closedAt?: Timestamp;
  readonly open: boolean;
  readonly commentsCount: number;
  readonly tags: Tag[];
}
