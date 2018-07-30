// this class is generated, please do not modify

export class NetworkType {

  constructor(public name?: string,
              public title?: string,
              public routeTagValues?: Array<string>) {
  }

  public static fromJSON(jsonObject): NetworkType {
    if (!jsonObject) {
      return undefined;
    }
    const instance = new NetworkType();
    instance.name = jsonObject.name;
    instance.title = jsonObject.title;
    instance.routeTagValues = jsonObject.routeTagValues;
    return instance;
  }
}

