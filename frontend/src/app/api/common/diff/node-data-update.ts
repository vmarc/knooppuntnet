// this file is generated, please do not modify

import { NodeMoved } from '@api/common/diff/node';
import { NodeData } from './node-data';
import { TagDiffs } from './tag-diffs';

export interface NodeDataUpdate {
  readonly before: NodeData;
  readonly after: NodeData;
  readonly tagDiffs: TagDiffs;
  readonly nodeMoved: NodeMoved;
}
