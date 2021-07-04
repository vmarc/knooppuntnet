// this class is generated, please do not modify

export class PageInfo {
  constructor(readonly country: string, readonly networkType: string) {}

  public static fromJSON(jsonObject: any): PageInfo {
    if (!jsonObject) {
      return undefined;
    }
    return new PageInfo(jsonObject.country, jsonObject.networkType);
  }
}
