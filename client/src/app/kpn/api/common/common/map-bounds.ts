// this class is generated, please do not modify

export class MapBounds {

  constructor(readonly latMin: string,
              readonly latMax: string,
              readonly lonMin: string,
              readonly lonMax: string) {
  }

  public static fromJSON(jsonObject: any): MapBounds {
    if (!jsonObject) {
      return undefined;
    }
    return new MapBounds(
      jsonObject.latMin,
      jsonObject.latMax,
      jsonObject.lonMin,
      jsonObject.lonMax
    );
  }
}
