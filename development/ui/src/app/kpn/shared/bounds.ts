// this class is generated, please do not modify

export class Bounds {

  constructor(public minLat?: number,
              public minLon?: number,
              public maxLat?: number,
              public maxLon?: number) {
  }

  public static fromJSON(jsonObject): Bounds {
    if (!jsonObject) {
      return undefined;
    }
    const instance = new Bounds();
    instance.minLat = jsonObject.minLat;
    instance.minLon = jsonObject.minLon;
    instance.maxLat = jsonObject.maxLat;
    instance.maxLon = jsonObject.maxLon;
    return instance;
  }
}

