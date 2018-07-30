// this class is generated, please do not modify

export class FactLevel {

  constructor(public name?: string) {
  }

  public static fromJSON(jsonObject): FactLevel {
    if (!jsonObject) {
      return undefined;
    }
    const instance = new FactLevel();
    instance.name = jsonObject.name;
    return instance;
  }
}

