// this class is generated, please do not modify

export class NetworkMapNode {
  constructor(
    readonly id: number,
    readonly name: string,
    readonly latitude: string,
    readonly longitude: string,
    readonly roleConnection: boolean
  ) {}

  public static fromJSON(jsonObject: any): NetworkMapNode {
    if (!jsonObject) {
      return undefined;
    }
    return new NetworkMapNode(
      jsonObject.id,
      jsonObject.name,
      jsonObject.latitude,
      jsonObject.longitude,
      jsonObject.roleConnection
    );
  }
}
