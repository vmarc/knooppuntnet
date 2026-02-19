// this file is generated, please do not modify

import { ChangeKey } from '@api/common/changes/details/change-key';
import { Subset } from '@api/custom/subset';
import { Timestamp } from '@api/custom/timestamp';
import { ChangeSetSubsetAnalysis } from './change-set-subset-analysis';
import { ChangeSetSubsetElementRefs } from './change-set-subset-element-refs';
import { LocationChanges } from './location-changes';
import { NetworkChanges } from './network-changes';

export interface ChangeSetSummary {
  readonly _id: string;
  readonly key: ChangeKey;
  readonly subsets: ReadonlyArray<Subset>;
  readonly locations: ReadonlyArray<string>;
  readonly timestampFrom: Timestamp;
  readonly timestampUntil: Timestamp;
  readonly networkChanges: NetworkChanges;
  readonly orphanRouteChanges: ReadonlyArray<ChangeSetSubsetElementRefs>;
  readonly orphanNodeChanges: ReadonlyArray<ChangeSetSubsetElementRefs>;
  readonly subsetAnalyses: ReadonlyArray<ChangeSetSubsetAnalysis>;
  readonly locationChanges: ReadonlyArray<LocationChanges>;
  readonly happy: boolean;
  readonly investigate: boolean;
  readonly impact: boolean;
}
