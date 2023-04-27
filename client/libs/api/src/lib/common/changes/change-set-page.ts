// this file is generated, please do not modify

import { ChangeSetSubsetElementRefs } from '@api/common';
import { ChangeSetSummary } from '@api/common';
import { NetworkChangeInfo } from '@api/common/changes/details';
import { KnownElements } from '@api/common/common';
import { NodeChangeInfo } from '@api/common/node';
import { RouteChangeInfo } from '@api/common/route';
import { ChangeSetInfo } from './change-set-info';

export interface ChangeSetPage {
  readonly summary: ChangeSetSummary;
  readonly changeSetInfo: ChangeSetInfo;
  readonly networkChanges: NetworkChangeInfo[];
  readonly orphanRouteChanges: ChangeSetSubsetElementRefs[];
  readonly orphanNodeChanges: ChangeSetSubsetElementRefs[];
  readonly routeChanges: RouteChangeInfo[];
  readonly nodeChanges: NodeChangeInfo[];
  readonly knownElements: KnownElements;
}
