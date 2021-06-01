// this file is generated, please do not modify

import { Fact } from '../../custom/fact';
import { NetworkType } from '../../custom/network-type';
import { NodeNetworkIntegrityCheck } from './node-network-integrity-check';
import { NodeNetworkRouteReference } from './node-network-route-reference';

export interface NodeNetworkReference {
  readonly networkType: NetworkType;
  readonly networkId: number;
  readonly networkName: string;
  readonly nodeDefinedInRelation: boolean;
  readonly nodeConnection: boolean;
  readonly nodeRoleConnection: boolean;
  readonly nodeIntegrityCheck: NodeNetworkIntegrityCheck;
  readonly facts: Fact[];
  readonly routes: NodeNetworkRouteReference[];
}
