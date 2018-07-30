// this class is generated, please do not modify

export class Country {

  constructor(public domain?: string) {
  }

  public static fromJSON(jsonObject): Country {
    if (!jsonObject) {
      return undefined;
    }
    const instance = new Country();
    instance.domain = jsonObject.domain;
    return instance;
  }
}

