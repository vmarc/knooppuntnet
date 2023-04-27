// this file is generated, please do not modify

import { NetworkChanges } from '.';
import { ChangeSetSubsetElementRefs } from './change-set-subset-element-refs';

export interface ChangeSetSummaryNetworkInfo {
  readonly networkChanges: NetworkChanges;
  readonly orphanRouteChanges: ChangeSetSubsetElementRefs[];
  readonly orphanNodeChanges: ChangeSetSubsetElementRefs[];
}
