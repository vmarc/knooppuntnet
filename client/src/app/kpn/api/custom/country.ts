export class Country {
  constructor(readonly domain: string) {}

  public static fromJSON(jsonObject: any): Country {
    if (!jsonObject) {
      return undefined;
    }
    return new Country(jsonObject);
  }
}
