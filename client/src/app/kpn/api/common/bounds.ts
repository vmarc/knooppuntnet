// this class is generated, please do not modify

export class Bounds {
  constructor(
    readonly minLat: number,
    readonly minLon: number,
    readonly maxLat: number,
    readonly maxLon: number
  ) {}

  public static fromJSON(jsonObject: any): Bounds {
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
