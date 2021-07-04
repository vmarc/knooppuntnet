// this class is generated, please do not modify

import { List } from 'immutable';
import { Fact } from '../../custom/fact';
import { NetworkType } from '../../custom/network-type';
import { NodeNetworkIntegrityCheck } from './node-network-integrity-check';
import { NodeNetworkRouteReference } from './node-network-route-reference';

export class NodeNetworkReference {
  constructor(
    readonly networkType: NetworkType,
    readonly networkId: number,
    readonly networkName: string,
    readonly nodeDefinedInRelation: boolean,
    readonly nodeConnection: boolean,
    readonly nodeRoleConnection: boolean,
    readonly nodeIntegrityCheck: NodeNetworkIntegrityCheck,
    readonly facts: List<Fact>,
    readonly routes: List<NodeNetworkRouteReference>
  ) {}

  public static fromJSON(jsonObject: any): NodeNetworkReference {
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
      jsonObject.facts
        ? List(jsonObject.facts.map((json: any) => Fact.fromJSON(json)))
        : List(),
      jsonObject.routes
        ? List(
            jsonObject.routes.map((json: any) =>
              NodeNetworkRouteReference.fromJSON(json)
            )
          )
        : List()
    );
  }
}
