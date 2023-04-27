// this file is generated, please do not modify

import { ChangeSetSummaryInfo } from '.';
import { ChangesFilterOption } from './changes/filter';

export interface ChangesPage {
  readonly filterOptions: ChangesFilterOption[];
  readonly changes: ChangeSetSummaryInfo[];
  readonly changeCount: number;
}
