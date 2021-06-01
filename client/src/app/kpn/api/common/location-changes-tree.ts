// this file is generated, please do not modify

import { LocationChangesTreeNode } from './location-changes-tree-node';
import { NetworkType } from '../custom/network-type';

export interface LocationChangesTree {
  readonly networkType: NetworkType;
  readonly locationName: string;
  readonly happy: boolean;
  readonly investigate: boolean;
  readonly children: LocationChangesTreeNode[];
}
