// this file is generated, please do not modify

import { OrphanNodeInfo } from '@api/common';
import { TimeInfo } from '@api/common';
import { SubsetInfo } from './subset-info';

export interface SubsetOrphanNodesPage {
  readonly timeInfo: TimeInfo;
  readonly subsetInfo: SubsetInfo;
  readonly nodes: OrphanNodeInfo[];
}
