// this file is generated, please do not modify

import { ChangeSetInfo } from './change-set-info';
import { ChangeSetSubsetElementRefs } from '../change-set-subset-element-refs';
import { ChangeSetSummary } from '../change-set-summary';
import { KnownElements } from '../common/known-elements';
import { LocationTreeItem } from '../location-tree-item';
import { NetworkChangeInfo } from './details/network-change-info';
import { NodeChangeInfo } from '../node/node-change-info';
import { RouteChangeInfo } from '../route/route-change-info';

export interface ChangeSetPage {
  readonly summary: ChangeSetSummary;
  readonly changeSetInfo: ChangeSetInfo;
  readonly networkChanges: NetworkChangeInfo[];
  readonly orphanRouteChanges: ChangeSetSubsetElementRefs[];
  readonly orphanNodeChanges: ChangeSetSubsetElementRefs[];
  readonly routeChanges: RouteChangeInfo[];
  readonly nodeChanges: NodeChangeInfo[];
  readonly knownElements: KnownElements;
  readonly treeItems: LocationTreeItem[];
}
