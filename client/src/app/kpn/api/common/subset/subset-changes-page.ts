// this file is generated, please do not modify

import { ChangeSetSummaryInfo } from '../change-set-summary-info';
import { ChangesFilterOption } from '../changes/filter/changes-filter-option';
import { SubsetInfo } from './subset-info';

export interface SubsetChangesPage {
  readonly subsetInfo: SubsetInfo;
  readonly filterOptions: ChangesFilterOption[];
  readonly changes: ChangeSetSummaryInfo[];
  readonly changeCount: number;
}
