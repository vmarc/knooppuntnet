// this class is generated, please do not modify

export class LatLonImpl {
  readonly latitude: string;
  readonly longitude: string;

  constructor(latitude: string,
              longitude: string) {
    this.latitude = latitude;
    this.longitude = longitude;
  }

  public static fromJSON(jsonObject): LatLonImpl {
    if (!jsonObject) {
      return undefined;
    }
    return new LatLonImpl(
      jsonObject.latitude,
      jsonObject.longitude
    );
  }
}
