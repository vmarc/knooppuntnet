// this file is generated, please do not modify

import { TagDiffs } from '.';
import { NodeMoved } from './node';
import { NodeData } from './node-data';

export interface NodeDataUpdate {
  readonly before: NodeData;
  readonly after: NodeData;
  readonly tagDiffs: TagDiffs;
  readonly nodeMoved: NodeMoved;
}
