// this class is generated, please do not modify

export class LatLonImpl {
  constructor(readonly latitude: string, readonly longitude: string) {}

  public static fromJSON(jsonObject: any): LatLonImpl {
    if (!jsonObject) {
      return undefined;
    }
    return new LatLonImpl(jsonObject.latitude, jsonObject.longitude);
  }
}
