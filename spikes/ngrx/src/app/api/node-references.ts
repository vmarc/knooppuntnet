import {NodeNetworkReference} from './node-network-reference';
import {NodeOrphanRouteReference} from './node-orphan-route-reference';

export interface NodeReferences {
  readonly networkReferences: NodeNetworkReference[];
  readonly routeReferences: NodeOrphanRouteReference[];
}
