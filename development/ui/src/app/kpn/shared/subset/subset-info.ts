// this class is generated, please do not modify

export class SubsetInfo {
  readonly country: string;
  readonly networkType: string;
  readonly networkCount: number;
  readonly factCount: number;
  readonly changesCount: number;
  readonly orphanNodeCount: number;
  readonly orphanRouteCount: number;

  constructor(country: string,
              networkType: string,
              networkCount: number,
              factCount: number,
              changesCount: number,
              orphanNodeCount: number,
              orphanRouteCount: number) {
    this.country = country;
    this.networkType = networkType;
    this.networkCount = networkCount;
    this.factCount = factCount;
    this.changesCount = changesCount;
    this.orphanNodeCount = orphanNodeCount;
    this.orphanRouteCount = orphanRouteCount;
  }

  public static fromJSON(jsonObject): SubsetInfo {
    if (!jsonObject) {
      return undefined;
    }
    return new SubsetInfo(
      jsonObject.country,
      jsonObject.networkType,
      jsonObject.networkCount,
      jsonObject.factCount,
      jsonObject.changesCount,
      jsonObject.orphanNodeCount,
      jsonObject.orphanRouteCount
    );
  }
}
