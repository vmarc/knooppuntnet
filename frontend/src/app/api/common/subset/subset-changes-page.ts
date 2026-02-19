// this file is generated, please do not modify

import { ChangeSetSummaryInfo } from '@api/common/change-set-summary-info';
import { ChangesFilterOption } from '@api/common/changes/filter/changes-filter-option';
import { SubsetInfo } from './subset-info';

export interface SubsetChangesPage {
  readonly subsetInfo: SubsetInfo;
  readonly filterOptions: ReadonlyArray<ChangesFilterOption>;
  readonly changes: ReadonlyArray<ChangeSetSummaryInfo>;
  readonly changeCount: number;
}
