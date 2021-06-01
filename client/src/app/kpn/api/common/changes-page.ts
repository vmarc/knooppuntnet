// this file is generated, please do not modify

import { ChangeSetSummaryInfo } from './change-set-summary-info';
import { ChangesFilter } from './changes/filter/changes-filter';

export interface ChangesPage {
  readonly filter: ChangesFilter;
  readonly changes: ChangeSetSummaryInfo[];
  readonly changeCount: number;
}
