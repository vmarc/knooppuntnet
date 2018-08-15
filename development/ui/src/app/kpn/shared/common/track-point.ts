// this class is generated, please do not modify

export class TrackPoint {

  constructor(public lat?: string,
              public lon?: string) {
  }

  public static fromJSON(jsonObject): TrackPoint {
    if (!jsonObject) {
      return undefined;
    }
    const instance = new TrackPoint();
    instance.lat = jsonObject.lat;
    instance.lon = jsonObject.lon;
    return instance;
  }
}

