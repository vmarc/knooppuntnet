// this class is generated, please do not modify

export class PlanCoordinate {
  constructor(readonly x: number, readonly y: number) {}

  public static fromJSON(jsonObject: any): PlanCoordinate {
    if (!jsonObject) {
      return undefined;
    }
    return new PlanCoordinate(jsonObject.x, jsonObject.y);
  }
}
