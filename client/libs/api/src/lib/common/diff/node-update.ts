// this file is generated, please do not modify

import { TagDiffs } from '.';
import { RawNode } from '../data/raw';
import { NodeMoved } from './node/node-moved';

export interface NodeUpdate {
  readonly before: RawNode;
  readonly after: RawNode;
  readonly tagDiffs: TagDiffs;
  readonly nodeMoved: NodeMoved;
}
