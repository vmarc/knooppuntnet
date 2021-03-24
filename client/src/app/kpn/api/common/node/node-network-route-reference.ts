// this class is generated, please do not modify

export class NodeNetworkRouteReference {

  constructor(readonly routeId: number,
              readonly routeName: string,
              readonly routeRole: string) {
  }

  static fromJSON(jsonObject: any): NodeNetworkRouteReference {
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
