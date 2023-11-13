// this file is generated, please do not modify

import { ChangeSetSummary } from '@api/common';
import { NetworkInfoChange } from '@api/common/changes/details';
import { NodeChange } from '@api/common/changes/details';
import { RouteChange } from '@api/common/changes/details';

export interface ChangeSetData {
  readonly summary: ChangeSetSummary;
  readonly networkChanges: NetworkInfoChange[];
  readonly routeChanges: RouteChange[];
  readonly nodeChanges: NodeChange[];
}
