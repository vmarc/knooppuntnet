export class MapPosition {
  constructor(
    readonly zoom: number,
    readonly x: number,
    readonly y: number,
    readonly rotation: number
  ) {}

  public static fromJSON(jsonObject: any): MapPosition {
    if (!jsonObject) {
      return undefined;
    }
    return new MapPosition(
      jsonObject.zoom,
      jsonObject.x,
      jsonObject.y,
      jsonObject.rotation
    );
  }
}
