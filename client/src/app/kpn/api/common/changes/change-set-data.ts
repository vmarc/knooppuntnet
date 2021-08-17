// this file is generated, please do not modify

import { ChangeSetSummary } from '../change-set-summary';
import { LocationChangeSetSummary } from '../location-change-set-summary';
import { NetworkInfoChange } from './details/network-info-change';
import { NodeChange } from './details/node-change';
import { RouteChange } from './details/route-change';

export interface ChangeSetData {
  readonly summary: ChangeSetSummary;
  readonly locationSummary: LocationChangeSetSummary;
  readonly networkChanges: NetworkInfoChange[];
  readonly routeChanges: RouteChange[];
  readonly nodeChanges: NodeChange[];
}
