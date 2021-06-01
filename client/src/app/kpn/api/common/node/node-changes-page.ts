// this file is generated, please do not modify

import { ChangesFilter } from '../changes/filter/changes-filter';
import { NodeChangeInfo } from './node-change-info';
import { NodeInfo } from '../node-info';

export interface NodeChangesPage {
  readonly nodeInfo: NodeInfo;
  readonly filter: ChangesFilter;
  readonly changes: NodeChangeInfo[];
  readonly incompleteWarning: boolean;
  readonly totalCount: number;
  readonly changeCount: number;
}
