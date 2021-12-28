// this file is generated, please do not modify

import { ChangesFilterOption } from '../changes/filter/changes-filter-option';
import { NetworkChangeInfo } from '../changes/details/network-change-info';
import { NetworkSummary } from './network-summary';

export interface NetworkChangesPage {
  readonly network: NetworkSummary;
  readonly filterOptions: ChangesFilterOption[];
  readonly changes: NetworkChangeInfo[];
  readonly totalCount: number;
}
