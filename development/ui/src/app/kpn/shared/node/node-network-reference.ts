// this class is generated, please do not modify

import {List} from 'immutable';
import {Fact} from '../fact';
import {NetworkType} from '../network-type';
import {NodeNetworkIntegrityCheck} from './node-network-integrity-check';
import {NodeNetworkRouteReference} from './node-network-route-reference';

export class NodeNetworkReference {
  readonly networkType: NetworkType;
  readonly networkId: number;
  readonly networkName: string;
  readonly nodeDefinedInRelation: boolean;
  readonly nodeConnection: boolean;
  readonly nodeRoleConnection: boolean;
  readonly nodeIntegrityCheck: NodeNetworkIntegrityCheck;
  readonly facts: List<Fact>;
  readonly routes: List<NodeNetworkRouteReference>;

  constructor(networkType: NetworkType,
              networkId: number,
              networkName: string,
              nodeDefinedInRelation: boolean,
              nodeConnection: boolean,
              nodeRoleConnection: boolean,
              nodeIntegrityCheck: NodeNetworkIntegrityCheck,
              facts: List<Fact>,
              routes: List<NodeNetworkRouteReference>) {
    this.networkType = networkType;
    this.networkId = networkId;
    this.networkName = networkName;
    this.nodeDefinedInRelation = nodeDefinedInRelation;
    this.nodeConnection = nodeConnection;
    this.nodeRoleConnection = nodeRoleConnection;
    this.nodeIntegrityCheck = nodeIntegrityCheck;
    this.facts = facts;
    this.routes = routes;
  }

  public static fromJSON(jsonObject): NodeNetworkReference {
    if (!jsonObject) {
      return undefined;
    }
    return new NodeNetworkReference(
      NetworkType.fromJSON(jsonObject.networkType),
      jsonObject.networkId,
      jsonObject.networkName,
      jsonObject.nodeDefinedInRelation,
      jsonObject.nodeConnection,
      jsonObject.nodeRoleConnection,
      NodeNetworkIntegrityCheck.fromJSON(jsonObject.nodeIntegrityCheck),
      jsonObject.facts ? List(jsonObject.facts.map(json => Fact.fromJSON(json))) : List(),
      jsonObject.routes ? List(jsonObject.routes.map(json => NodeNetworkRouteReference.fromJSON(json))) : List()
    );
  }
}
