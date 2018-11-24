// this class is generated, please do not modify

export class NodeNetworkRouteReference {
  readonly routeId: number;
  readonly routeName: string;
  readonly routeRole: string;

  constructor(routeId: number,
              routeName: string,
              routeRole: string) {
    this.routeId = routeId;
    this.routeName = routeName;
    this.routeRole = routeRole;
  }

  public static fromJSON(jsonObject): NodeNetworkRouteReference {
    if (!jsonObject) {
      return undefined;
    }
    return new NodeNetworkRouteReference(
      jsonObject.routeId,
      jsonObject.routeName,
      jsonObject.routeRole
    );
  }
}
