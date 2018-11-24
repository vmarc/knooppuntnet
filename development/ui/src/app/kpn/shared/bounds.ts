// this class is generated, please do not modify

export class Bounds {
  readonly minLat: number;
  readonly minLon: number;
  readonly maxLat: number;
  readonly maxLon: number;

  constructor(minLat: number,
              minLon: number,
              maxLat: number,
              maxLon: number) {
    this.minLat = minLat;
    this.minLon = minLon;
    this.maxLat = maxLat;
    this.maxLon = maxLon;
  }

  public static fromJSON(jsonObject): Bounds {
    if (!jsonObject) {
      return undefined;
    }
    return new Bounds(
      jsonObject.minLat,
      jsonObject.minLon,
      jsonObject.maxLat,
      jsonObject.maxLon
    );
  }
}
