// this class is generated, please do not modify

export class LatLonImpl {

  constructor(public latitude?: string,
              public longitude?: string) {
  }

  public static fromJSON(jsonObject): LatLonImpl {
    if (!jsonObject) {
      return undefined;
    }
    const instance = new LatLonImpl();
    instance.latitude = jsonObject.latitude;
    instance.longitude = jsonObject.longitude;
    return instance;
  }
}

