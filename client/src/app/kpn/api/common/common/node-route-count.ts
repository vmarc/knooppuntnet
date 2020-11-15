// this class is generated, please do not modify

export class NodeRouteCount {

  constructor(readonly nodeId: number,
              readonly routeCount: number) {
  }

  public static fromJSON(jsonObject: any): NodeRouteCount {
    if (!jsonObject) {
      return undefined;
    }
    return new NodeRouteCount(
      jsonObject.nodeId,
      jsonObject.routeCount
    );
  }
}
