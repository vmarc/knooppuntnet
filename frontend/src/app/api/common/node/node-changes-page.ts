// this file is generated, please do not modify

import { ChangesFilterOption } from '@api/common/changes/filter/changes-filter-option';
import { NodeChangeInfo } from './node-change-info';

export interface NodeChangesPage {
  readonly nodeId: number;
  readonly nodeName: string;
  readonly filterOptions: ReadonlyArray<ChangesFilterOption>;
  readonly changes: ReadonlyArray<NodeChangeInfo>;
  readonly totalCount: number;
  readonly changeCount: number;
}
