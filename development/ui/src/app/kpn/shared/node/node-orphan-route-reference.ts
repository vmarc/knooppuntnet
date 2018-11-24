// this class is generated, please do not modify

import {NetworkType} from '../network-type';

export class NodeOrphanRouteReference {
  readonly networkType: NetworkType;
  readonly routeId: number;
  readonly routeName: string;

  constructor(networkType: NetworkType,
              routeId: number,
              routeName: string) {
    this.networkType = networkType;
    this.routeId = routeId;
    this.routeName = routeName;
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
