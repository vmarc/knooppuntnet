// this class is generated, please do not modify

export class MapBounds {
  readonly latMin: string;
  readonly latMax: string;
  readonly lonMin: string;
  readonly lonMax: string;

  constructor(latMin: string,
              latMax: string,
              lonMin: string,
              lonMax: string) {
    this.latMin = latMin;
    this.latMax = latMax;
    this.lonMin = lonMin;
    this.lonMax = lonMax;
  }

  public static fromJSON(jsonObject): MapBounds {
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
