// this file is generated, please do not modify

import { ChangeSetSummary } from '@api/common/change-set-summary';
import { NetworkInfoChange } from '@api/common/changes/details/network-info-change';
import { NodeChange } from '@api/common/changes/details/node-change';
import { RouteChange } from '@api/common/changes/details/route-change';

export interface ChangeSetData {
  readonly summary: ChangeSetSummary;
  readonly networkChanges: NetworkInfoChange[];
  readonly routeChanges: RouteChange[];
  readonly nodeChanges: NodeChange[];
}
