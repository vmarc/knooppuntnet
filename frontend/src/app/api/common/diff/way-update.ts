// this file is generated, please do not modify

import { MetaData } from '@api/common/data/meta-data';
import { Node } from '@api/common/data/node';
import { NodeUpdate } from './node-update';
import { TagDiffs } from './tag-diffs';

export interface WayUpdate {
  readonly id: number;
  readonly before: MetaData;
  readonly after: MetaData;
  readonly removedNodes: Node[];
  readonly addedNodes: Node[];
  readonly updatedNodes: NodeUpdate[];
  readonly directionReversed: boolean;
  readonly tagDiffs?: TagDiffs;
}
