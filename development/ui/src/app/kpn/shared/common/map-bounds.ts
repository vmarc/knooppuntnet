// this class is generated, please do not modify

export class MapBounds {

  constructor(public latMin?: string,
              public latMax?: string,
              public lonMin?: string,
              public lonMax?: string) {
  }

  public static fromJSON(jsonObject): MapBounds {
    if (!jsonObject) {
      return undefined;
    }
    const instance = new MapBounds();
    instance.latMin = jsonObject.latMin;
    instance.latMax = jsonObject.latMax;
    instance.lonMin = jsonObject.lonMin;
    instance.lonMax = jsonObject.lonMax;
    return instance;
  }
}

