// this file is generated, please do not modify

import { ChangesFilterOption } from '../changes/filter/changes-filter-option';
import { NodeChangeInfo } from './node-change-info';

export interface NodeChangesPage {
  readonly nodeId: number;
  readonly nodeName: string;
  readonly filterOptions: ChangesFilterOption[];
  readonly changes: NodeChangeInfo[];
  readonly totalCount: number;
  readonly changeCount: number;
}
