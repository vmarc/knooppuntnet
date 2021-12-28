// this file is generated, please do not modify

import { ChangeSetSummaryInfo } from './change-set-summary-info';
import { ChangesFilterOption } from './changes/filter/changes-filter-option';

export interface ChangesPage {
  readonly filterOptions: ChangesFilterOption[];
  readonly changes: ChangeSetSummaryInfo[];
  readonly changeCount: number;
}
