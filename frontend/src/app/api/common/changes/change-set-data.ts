// this file is generated, please do not modify

import { ChangeSetSummary } from '@api/common/change-set-summary';
import { BaseRouteChange } from '@api/common/changes/details/base-route-change';
import { NetworkChange } from '@api/common/changes/details/network-change';
import { NodeChange } from '@api/common/changes/details/node-change';
import { RouteChange } from '@api/common/changes/details/route-change';

export interface ChangeSetData {
  readonly summary: ChangeSetSummary;
  readonly networkChanges: NetworkChange[];
  readonly baseRouteChanges: BaseRouteChange[];
  readonly routeChanges: RouteChange[];
  readonly nodeChanges: NodeChange[];
}
