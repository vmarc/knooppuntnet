// this file is generated, please do not modify

import { OrphanNodeInfo } from '@api/common/orphan-node-info';
import { TimeInfo } from '@api/common/time-info';
import { SubsetInfo } from './subset-info';

export interface SubsetOrphanNodesPage {
  readonly timeInfo: TimeInfo;
  readonly subsetInfo: SubsetInfo;
  readonly nodes: ReadonlyArray<OrphanNodeInfo>;
}
