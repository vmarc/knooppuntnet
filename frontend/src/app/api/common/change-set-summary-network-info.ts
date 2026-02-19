// this file is generated, please do not modify

import { ChangeSetSubsetElementRefs } from './change-set-subset-element-refs';
import { NetworkChanges } from './network-changes';

export interface ChangeSetSummaryNetworkInfo {
  readonly networkChanges: NetworkChanges;
  readonly orphanRouteChanges: ReadonlyArray<ChangeSetSubsetElementRefs>;
  readonly orphanNodeChanges: ReadonlyArray<ChangeSetSubsetElementRefs>;
}
