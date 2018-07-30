// this class is generated, please do not modify

export class SubsetInfo {

  constructor(public country?: string,
              public networkType?: string,
              public networkCount?: number,
              public factCount?: number,
              public changesCount?: number,
              public orphanNodeCount?: number,
              public orphanRouteCount?: number) {
  }

  public static fromJSON(jsonObject): SubsetInfo {
    if (!jsonObject) {
      return undefined;
    }
    const instance = new SubsetInfo();
    instance.country = jsonObject.country;
    instance.networkType = jsonObject.networkType;
    instance.networkCount = jsonObject.networkCount;
    instance.factCount = jsonObject.factCount;
    instance.changesCount = jsonObject.changesCount;
    instance.orphanNodeCount = jsonObject.orphanNodeCount;
    instance.orphanRouteCount = jsonObject.orphanRouteCount;
    return instance;
  }
}

