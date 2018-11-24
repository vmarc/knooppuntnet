// this class is generated, please do not modify

export class TrackPoint {
  readonly lat: string;
  readonly lon: string;

  constructor(lat: string,
              lon: string) {
    this.lat = lat;
    this.lon = lon;
  }

  public static fromJSON(jsonObject): TrackPoint {
    if (!jsonObject) {
      return undefined;
    }
    return new TrackPoint(
      jsonObject.lat,
      jsonObject.lon
    );
  }
}
