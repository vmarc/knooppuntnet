// this class is generated, please do not modify

import {NodeNetworkReference} from './node-network-reference';
import {NodeOrphanRouteReference} from './node-orphan-route-reference';

export class NodeReferences {

  constructor(readonly networkReferences: Array<NodeNetworkReference>,
              readonly routeReferences: Array<NodeOrphanRouteReference>) {
  }

  public static fromJSON(jsonObject: any): NodeReferences {
    if (!jsonObject) {
      return undefined;
    }
    return new NodeReferences(
      jsonObject.networkReferences.map((json: any) => NodeNetworkReference.fromJSON(json)),
      jsonObject.routeReferences.map((json: any) => NodeOrphanRouteReference.fromJSON(json))
    );
  }
}
