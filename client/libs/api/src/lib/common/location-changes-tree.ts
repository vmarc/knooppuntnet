// this file is generated, please do not modify

import { NetworkType } from '@api/custom';
import { LocationChangesTreeNode } from './location-changes-tree-node';

export interface LocationChangesTree {
  readonly networkType: NetworkType;
  readonly locationName: string;
  readonly happy: boolean;
  readonly investigate: boolean;
  readonly children: LocationChangesTreeNode[];
}
