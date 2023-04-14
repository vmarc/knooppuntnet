// this file is generated, please do not modify

import { NodeUpdate } from '.';
import { MetaData } from '../data';
import { RawNode } from '../data/raw';
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
