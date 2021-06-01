// this file is generated, please do not modify

import { Tags } from '../../custom/tags';
import { Timestamp } from '../../custom/timestamp';

export interface ChangeSetInfo {
  readonly id: number;
  readonly createdAt: Timestamp;
  readonly closedAt: Timestamp;
  readonly open: boolean;
  readonly commentsCount: number;
  readonly tags: Tags;
}
