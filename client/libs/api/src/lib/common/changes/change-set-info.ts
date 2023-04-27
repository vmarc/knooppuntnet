// this file is generated, please do not modify

import { Tags } from '@api/custom';
import { Timestamp } from '@api/custom';

export interface ChangeSetInfo {
  readonly _id: number;
  readonly id: number;
  readonly createdAt: Timestamp;
  readonly closedAt: Timestamp;
  readonly open: boolean;
  readonly commentsCount: number;
  readonly tags: Tags;
}
