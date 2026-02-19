// this file is generated, please do not modify

import { Tag } from '@api/custom/tag';
import { Timestamp } from '@api/custom/timestamp';
import { Node } from './node';

export interface Way {
  readonly id: number;
  readonly version: number;
  readonly timestamp: Timestamp;
  readonly changeSetId: number;
  readonly tags: ReadonlyArray<Tag>;
  readonly nodes: ReadonlyArray<Node>;
  readonly length: number;
}
