// this class is generated, please do not modify

export class RouteNetworkNodeInfo {
  readonly id: number;
  readonly name: string;
  readonly alternateName: string;
  readonly lat: string;
  readonly lon: string;

  constructor(id: number,
              name: string,
              alternateName: string,
              lat: string,
              lon: string) {
    this.id = id;
    this.name = name;
    this.alternateName = alternateName;
    this.lat = lat;
    this.lon = lon;
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
      jsonObject.lon
    );
  }
}
