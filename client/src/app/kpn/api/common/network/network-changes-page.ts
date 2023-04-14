// this file is generated, please do not modify

import { NetworkChangeInfo } from '@api/common/changes/details';
import { ChangesFilterOption } from '@api/common/changes/filter';
import { NetworkSummary } from './network-summary';

export interface NetworkChangesPage {
  readonly network: NetworkSummary;
  readonly filterOptions: ChangesFilterOption[];
  readonly changes: NetworkChangeInfo[];
  readonly totalCount: number;
}
