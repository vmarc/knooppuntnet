// this file is generated, please do not modify

import { OrphanNodeInfo } from '../orphan-node-info';
import { SubsetInfo } from './subset-info';
import { TimeInfo } from '../time-info';

export interface SubsetOrphanNodesPage {
  readonly timeInfo: TimeInfo;
  readonly subsetInfo: SubsetInfo;
  readonly nodes: OrphanNodeInfo[];
}
