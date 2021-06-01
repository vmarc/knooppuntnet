// this file is generated, please do not modify

import { ChangeSetSummary } from '../change-set-summary';
import { NetworkChange } from './details/network-change';
import { NodeChange } from './details/node-change';
import { RouteChange } from './details/route-change';

export interface ChangeSetData {
  readonly summary: ChangeSetSummary;
  readonly networkChanges: NetworkChange[];
  readonly routeChanges: RouteChange[];
  readonly nodeChanges: NodeChange[];
}
