// this file is generated, please do not modify

import { Node } from '@api/common/data/node';
import { NodeMoved } from '@api/common/diff/node/node-moved';
import { TagDiffs } from './tag-diffs';

export interface NodeUpdate {
  readonly before: Node;
  readonly after: Node;
  readonly tagDiffs?: TagDiffs;
  readonly nodeMoved?: NodeMoved;
}
