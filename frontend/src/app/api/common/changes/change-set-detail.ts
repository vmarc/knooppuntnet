// this file is generated, please do not modify

import { ChangeSetSubsetElementRefs } from '@api/common/change-set-subset-element-refs';
import { ChangeSetSummary } from '@api/common/change-set-summary';
import { NetworkChangeInfo } from '@api/common/changes/details/network-change-info';
import { KnownElements } from '@api/common/common/known-elements';
import { NodeChangeInfo } from '@api/common/node/node-change-info';
import { RouteChangeInfo } from '@api/common/route/route-change-info';
import { ChangeSetInfo } from './change-set-info';

export interface ChangeSetDetail {
  readonly summary: ChangeSetSummary;
  readonly changeSetInfo?: ChangeSetInfo;
  readonly networkChanges: NetworkChangeInfo[];
  readonly orphanRouteChanges: ChangeSetSubsetElementRefs[];
  readonly orphanNodeChanges: ChangeSetSubsetElementRefs[];
  readonly routeChanges: RouteChangeInfo[];
  readonly nodeChanges: NodeChangeInfo[];
  readonly knownElements: KnownElements;
}
