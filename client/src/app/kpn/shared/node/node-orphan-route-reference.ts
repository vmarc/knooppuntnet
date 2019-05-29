// this class is generated, please do not modify

import {NetworkType} from "../network-type";

export class NodeOrphanRouteReference {

  constructor(readonly networkType: NetworkType,
              readonly routeId: number,
              readonly routeName: string) {
  }

  public static fromJSON(jsonObject): NodeOrphanRouteReference {
    if (!jsonObject) {
      return undefined;
    }
    return new NodeOrphanRouteReference(
      NetworkType.fromJSON(jsonObject.networkType),
      jsonObject.routeId,
      jsonObject.routeName
    );
  }
}
