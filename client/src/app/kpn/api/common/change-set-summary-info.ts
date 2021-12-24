// this file is generated, please do not modify

import { ChangeKey } from './changes/details/change-key';
import { ChangeSetSummaryLocationInfo } from './change-set-summary-location-info';
import { ChangeSetSummaryNetworkInfo } from './change-set-summary-network-info';
import { Subset } from '../custom/subset';

export interface ChangeSetSummaryInfo {
  readonly _id: string;
  readonly key: ChangeKey;
  readonly comment: string;
  readonly subsets: Subset[];
  readonly network: ChangeSetSummaryNetworkInfo;
  readonly location: ChangeSetSummaryLocationInfo;
  readonly happy: boolean;
  readonly investigate: boolean;
  readonly impact: boolean;
}
