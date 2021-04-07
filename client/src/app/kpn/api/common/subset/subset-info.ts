// this class is generated, please do not modify

export class SubsetInfo {
  constructor(
    readonly country: string,
    readonly networkType: string,
    readonly networkCount: number,
    readonly factCount: number,
    readonly changesCount: number,
    readonly orphanNodeCount: number,
    readonly orphanRouteCount: number
  ) {}

  public static fromJSON(jsonObject: any): SubsetInfo {
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
