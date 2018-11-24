export class Country {
  readonly domain: string;

  constructor(domain: string) {
    this.domain = domain;
  }

  public static fromJSON(jsonObject): Country {
    if (!jsonObject) {
      return undefined;
    }
    return new Country(
      jsonObject.domain
    );
  }
}
