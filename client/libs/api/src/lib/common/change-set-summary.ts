// this file is generated, please do not modify

import { ChangeKey } from '@api/common/changes/details';
import { Subset } from '@api/custom';
import { Timestamp } from '@api/custom';
import { LocationChanges } from '.';
import { NetworkChanges } from '.';
import { ChangeSetSubsetAnalysis } from './change-set-subset-analysis';
import { ChangeSetSubsetElementRefs } from './change-set-subset-element-refs';

export interface ChangeSetSummary {
  readonly _id: string;
  readonly key: ChangeKey;
  readonly subsets: Subset[];
  readonly timestampFrom: Timestamp;
  readonly timestampUntil: Timestamp;
  readonly networkChanges: NetworkChanges;
  readonly orphanRouteChanges: ChangeSetSubsetElementRefs[];
  readonly orphanNodeChanges: ChangeSetSubsetElementRefs[];
  readonly subsetAnalyses: ChangeSetSubsetAnalysis[];
  readonly locationChanges: LocationChanges[];
  readonly locations: string[];
  readonly happy: boolean;
  readonly investigate: boolean;
  readonly impact: boolean;
}
