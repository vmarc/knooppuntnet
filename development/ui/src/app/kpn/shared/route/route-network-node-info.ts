// this class is generated, please do not modify

export class RouteNetworkNodeInfo {

  constructor(public id?: number,
              public name?: string,
              public alternateName?: string,
              public lat?: string,
              public lon?: string) {
  }

  public static fromJSON(jsonObject): RouteNetworkNodeInfo {
    if (!jsonObject) {
      return undefined;
    }
    const instance = new RouteNetworkNodeInfo();
    instance.id = jsonObject.id;
    instance.name = jsonObject.name;
    instance.alternateName = jsonObject.alternateName;
    instance.lat = jsonObject.lat;
    instance.lon = jsonObject.lon;
    return instance;
  }
}

