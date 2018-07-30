// this class is generated, please do not modify

export class ChangeType {

  constructor(public name?: string) {
  }

  public static fromJSON(jsonObject): ChangeType {
    if (!jsonObject) {
      return undefined;
    }
    const instance = new ChangeType();
    instance.name = jsonObject.name;
    return instance;
  }
}

