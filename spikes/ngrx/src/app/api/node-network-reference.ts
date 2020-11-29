import {NodeNetworkIntegrityCheck} from './node-network-integrity-check';
import {NodeNetworkRouteReference} from './node-network-route-reference';
import {NetworkType} from './network-type';

export interface NodeNetworkReference {
  readonly networkType: NetworkType;
  readonly networkId: number;
  readonly networkName: string;
  readonly nodeDefinedInRelation: boolean;
  readonly nodeConnection: boolean;
  readonly nodeRoleConnection: boolean;
  readonly nodeIntegrityCheck: NodeNetworkIntegrityCheck;
  readonly facts: string[];
  readonly routes: NodeNetworkRouteReference[];
}
