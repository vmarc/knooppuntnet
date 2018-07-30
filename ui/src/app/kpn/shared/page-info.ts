// this class is generated, please do not modify

export class PageInfo {

  constructor(public country?: string,
              public networkType?: string) {
  }

  public static fromJSON(jsonObject): PageInfo {
    if (!jsonObject) {
      return undefined;
    }
    const instance = new PageInfo();
    instance.country = jsonObject.country;
    instance.networkType = jsonObject.networkType;
    return instance;
  }
}

