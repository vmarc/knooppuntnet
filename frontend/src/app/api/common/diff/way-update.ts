// this file is generated, please do not modify

import { MetaData } from '@api/common/data/meta-data';
import { TagDiffs } from './tag-diffs';

export interface WayUpdate {
  readonly id: number;
  readonly before: MetaData;
  readonly after: MetaData;
  readonly removedNodeIds: number[];
  readonly addedNodeIds: number[];
  readonly directionReversed: boolean;
  readonly tagDiffs?: TagDiffs;
}
