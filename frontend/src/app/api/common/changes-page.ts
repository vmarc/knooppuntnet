// this file is generated, please do not modify

import { ChangesFilterOption } from '@api/common/changes/filter';
import { ChangeSetSummaryInfo } from './change-set-summary-info';

export interface ChangesPage {
  readonly filterOptions: ChangesFilterOption[];
  readonly changes: ChangeSetSummaryInfo[];
  readonly changeCount: number;
}
