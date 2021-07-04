// this class is generated, please do not modify

import { List } from 'immutable';
import { NodeNetworkReference } from './node-network-reference';
import { NodeOrphanRouteReference } from './node-orphan-route-reference';

export class NodeReferences {
  constructor(
    readonly networkReferences: List<NodeNetworkReference>,
    readonly routeReferences: List<NodeOrphanRouteReference>
  ) {}

  public static fromJSON(jsonObject: any): NodeReferences {
    if (!jsonObject) {
      return undefined;
    }
    return new NodeReferences(
      jsonObject.networkReferences
        ? List(
            jsonObject.networkReferences.map((json: any) =>
              NodeNetworkReference.fromJSON(json)
            )
          )
        : List(),
      jsonObject.routeReferences
        ? List(
            jsonObject.routeReferences.map((json: any) =>
              NodeOrphanRouteReference.fromJSON(json)
            )
          )
        : List()
    );
  }
}
