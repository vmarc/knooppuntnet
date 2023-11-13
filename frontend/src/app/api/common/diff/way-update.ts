// this file is generated, please do not modify

import { MetaData } from '@api/common/data';
import { RawNode } from '@api/common/data/raw';
import { NodeUpdate } from './node-update';
import { TagDiffs } from './tag-diffs';

export interface WayUpdate {
  readonly id: number;
  readonly before: MetaData;
  readonly after: MetaData;
  readonly removedNodes: RawNode[];
  readonly addedNodes: RawNode[];
  readonly updatedNodes: NodeUpdate[];
  readonly directionReversed: boolean;
  readonly tagDiffs: TagDiffs;
}
