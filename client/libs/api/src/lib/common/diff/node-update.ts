// this file is generated, please do not modify

import { RawNode } from '@api/common/data/raw';
import { NodeMoved } from '@api/common/diff/node';
import { TagDiffs } from './tag-diffs';

export interface NodeUpdate {
  readonly before: RawNode;
  readonly after: RawNode;
  readonly tagDiffs: TagDiffs;
  readonly nodeMoved: NodeMoved;
}
