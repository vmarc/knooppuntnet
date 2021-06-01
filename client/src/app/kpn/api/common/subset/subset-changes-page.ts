// this file is generated, please do not modify

import { ChangeSetSummaryInfo } from '../change-set-summary-info';
import { ChangesFilter } from '../changes/filter/changes-filter';
import { SubsetInfo } from './subset-info';

export interface SubsetChangesPage {
  readonly subsetInfo: SubsetInfo;
  readonly filter: ChangesFilter;
  readonly changes: ChangeSetSummaryInfo[];
  readonly changeCount: number;
}
