// this class is generated, please do not modify

export class LocationNode {
  constructor(
    readonly name: string,
    readonly nodeCount: number,
    readonly children: Array<LocationNode>
  ) {}

  public static fromJSON(jsonObject: any): LocationNode {
    if (!jsonObject) {
      return undefined;
    }
    return new LocationNode(
      jsonObject.name,
      jsonObject.nodeCount,
      jsonObject.children?.map((json: any) => LocationNode.fromJSON(json))
    );
  }
}
