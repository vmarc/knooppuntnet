// this file is generated, please do not modify

import { ChangesFilter } from '../changes/filter/changes-filter';
import { NetworkChangeInfo } from '../changes/details/network-change-info';
import { NetworkSummary } from './network-summary';

export interface NetworkChangesPage {
  readonly network: NetworkSummary;
  readonly filter: ChangesFilter;
  readonly changes: NetworkChangeInfo[];
  readonly totalCount: number;
}
