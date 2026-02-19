// this file is generated, please do not modify

import { NetworkChangeInfo } from '@api/common/changes/details/network-change-info';
import { ChangesFilterOption } from '@api/common/changes/filter/changes-filter-option';
import { NetworkSummary } from './network-summary';

export interface NetworkChangesPage {
  readonly network: NetworkSummary;
  readonly filterOptions: ReadonlyArray<ChangesFilterOption>;
  readonly changes: ReadonlyArray<NetworkChangeInfo>;
  readonly totalCount: number;
}
