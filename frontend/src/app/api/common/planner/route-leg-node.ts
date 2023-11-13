// this class is generated, please do not modify

export class RouteLegNode {
  constructor(
    readonly nodeId: string,
    readonly nodeName: string,
    readonly lat: string,
    readonly lon: string
  ) {}

  static fromJSON(jsonObject: any): RouteLegNode {
    if (!jsonObject) {
      return undefined;
    }
    return new RouteLegNode(
      jsonObject.nodeId,
      jsonObject.nodeName,
      jsonObject.lat,
      jsonObject.lon
    );
  }
}
