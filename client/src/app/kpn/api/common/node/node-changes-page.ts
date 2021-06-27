// this file is generated, please do not modify

import { ChangesFilter } from '../changes/filter/changes-filter';
import { NodeChangeInfo } from './node-change-info';

export interface NodeChangesPage {
  readonly nodeId: number;
  readonly nodeName: string;
  readonly filter: ChangesFilter;
  readonly changes: NodeChangeInfo[];
  readonly incompleteWarning: boolean;
  readonly totalCount: number;
  readonly changeCount: number;
}
