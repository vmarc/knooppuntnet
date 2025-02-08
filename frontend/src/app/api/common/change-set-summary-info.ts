// this file is generated, please do not modify

import { ChangeKey } from '@api/common/changes/details/change-key';
import { Subset } from '@api/custom/subset';
import { ChangeSetSummaryLocationInfo } from './change-set-summary-location-info';
import { ChangeSetSummaryNetworkInfo } from './change-set-summary-network-info';

export interface ChangeSetSummaryInfo {
  readonly rowIndex: number;
  readonly key: ChangeKey;
  readonly comment: string;
  readonly subsets: Subset[];
  readonly network?: ChangeSetSummaryNetworkInfo;
  readonly location?: ChangeSetSummaryLocationInfo;
  readonly happy: boolean;
  readonly investigate: boolean;
  readonly impact: boolean;
}
