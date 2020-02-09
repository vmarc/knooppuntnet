// this class is generated, please do not modify

export class LocationSummary {

  constructor(readonly factCount: number,
              readonly nodeCount: number,
              readonly routeCount: number,
              readonly changeCount: number) {
  }

  public static fromJSON(jsonObject): LocationSummary {
    if (!jsonObject) {
      return undefined;
    }
    return new LocationSummary(
      jsonObject.factCount,
      jsonObject.nodeCount,
      jsonObject.routeCount,
      jsonObject.changeCount
    );
  }
}
