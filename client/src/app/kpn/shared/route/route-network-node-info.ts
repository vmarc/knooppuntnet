// this class is generated, please do not modify

export class RouteNetworkNodeInfo {

  constructor(readonly id: number,
              readonly name: string,
              readonly alternateName: string,
              readonly lat: string,
              readonly lon: string,
              readonly networkTypeTagged: boolean) {
  }

  public static fromJSON(jsonObject): RouteNetworkNodeInfo {
    if (!jsonObject) {
      return undefined;
    }
    return new RouteNetworkNodeInfo(
      jsonObject.id,
      jsonObject.name,
      jsonObject.alternateName,
      jsonObject.lat,
      jsonObject.lon,
      jsonObject.networkTypeTagged
    );
  }
}
