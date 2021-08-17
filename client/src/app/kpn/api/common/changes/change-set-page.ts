// this file is generated, please do not modify

import { ChangeSetInfo } from './change-set-info';
import { ChangeSetSummary } from '../change-set-summary';
import { KnownElements } from '../common/known-elements';
import { LocationChangesTree } from '../location-changes-tree';
import { NetworkChangeInfo } from './details/network-change-info';
import { NodeChangeInfo } from '../node/node-change-info';
import { RouteChangeInfo } from '../route/route-change-info';

export interface ChangeSetPage {
  readonly summary: ChangeSetSummary;
  readonly changeSetInfo: ChangeSetInfo;
  readonly trees: LocationChangesTree[];
  readonly networkChanges: NetworkChangeInfo[];
  readonly routeChanges: RouteChangeInfo[];
  readonly nodeChanges: NodeChangeInfo[];
  readonly knownElements: KnownElements;
}
