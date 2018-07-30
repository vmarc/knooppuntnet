export class Country {

  constructor(public domain?: string) {
  }

  public static fromJSON(jsonObject): Country {
    if (!jsonObject) {
      return undefined;
    }
    const instance = new Country();
    instance.domain =  jsonObject;
    return instance;
  }
}

