// this file is generated, please do not modify

import { ChangeKey } from './changes/details/change-key';
import { ChangeSetSubsetAnalysis } from './change-set-subset-analysis';
import { ChangeSetSubsetElementRefs } from './change-set-subset-element-refs';
import { NetworkChanges } from './network-changes';
import { Subset } from '../custom/subset';
import { Timestamp } from '../custom/timestamp';

export interface ChangeSetSummary {
  readonly _id: string;
  readonly key: ChangeKey;
  readonly subsets: Subset[];
  readonly timestampFrom: Timestamp;
  readonly timestampUntil: Timestamp;
  readonly networkChanges: NetworkChanges;
  readonly routeChanges: ChangeSetSubsetElementRefs[];
  readonly nodeChanges: ChangeSetSubsetElementRefs[];
  readonly subsetAnalyses: ChangeSetSubsetAnalysis[];
  readonly happy: boolean;
  readonly investigate: boolean;
  readonly impact: boolean;
}
