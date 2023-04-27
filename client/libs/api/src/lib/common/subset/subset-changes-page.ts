// this file is generated, please do not modify

import { ChangeSetSummaryInfo } from '@api/common';
import { ChangesFilterOption } from '@api/common/changes/filter';
import { SubsetInfo } from './subset-info';

export interface SubsetChangesPage {
  readonly subsetInfo: SubsetInfo;
  readonly filterOptions: ChangesFilterOption[];
  readonly changes: ChangeSetSummaryInfo[];
  readonly changeCount: number;
}
