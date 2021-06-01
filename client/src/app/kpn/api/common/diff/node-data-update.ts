// this file is generated, please do not modify

import { NodeData } from './node-data';
import { NodeMoved } from './node/node-moved';
import { TagDiffs } from './tag-diffs';

export interface NodeDataUpdate {
  readonly before: NodeData;
  readonly after: NodeData;
  readonly tagDiffs: TagDiffs;
  readonly nodeMoved: NodeMoved;
}
