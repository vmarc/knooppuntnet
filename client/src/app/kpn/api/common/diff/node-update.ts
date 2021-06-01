// this file is generated, please do not modify

import { NodeMoved } from './node/node-moved';
import { RawNode } from '../data/raw/raw-node';
import { TagDiffs } from './tag-diffs';

export interface NodeUpdate {
  readonly before: RawNode;
  readonly after: RawNode;
  readonly tagDiffs: TagDiffs;
  readonly nodeMoved: NodeMoved;
}
