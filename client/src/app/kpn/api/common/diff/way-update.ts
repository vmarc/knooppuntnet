// this file is generated, please do not modify

import { MetaData } from '../data/meta-data';
import { NodeUpdate } from './node-update';
import { RawNode } from '../data/raw/raw-node';
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
