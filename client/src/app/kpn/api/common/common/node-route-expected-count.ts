// this class is generated, please do not modify

export class NodeRouteExpectedCount {

  constructor(readonly nodeId: number,
              readonly nodeName: string,
              readonly locationNames: Array<string>,
              readonly routeCount: number) {
  }

  public static fromJSON(jsonObject: any): NodeRouteExpectedCount {
    if (!jsonObject) {
      return undefined;
    }
    return new NodeRouteExpectedCount(
      jsonObject.nodeId,
      jsonObject.nodeName,
      jsonObject.locationNames,
      jsonObject.routeCount
    );
  }
}
