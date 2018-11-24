// this class is generated, please do not modify

export class PageInfo {
  readonly country: string;
  readonly networkType: string;

  constructor(country: string,
              networkType: string) {
    this.country = country;
    this.networkType = networkType;
  }

  public static fromJSON(jsonObject): PageInfo {
    if (!jsonObject) {
      return undefined;
    }
    return new PageInfo(
      jsonObject.country,
      jsonObject.networkType
    );
  }
}
