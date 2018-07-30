// this class is generated, please do not modify

export class TagDetailType {

  constructor(public name?: string) {
  }

  public static fromJSON(jsonObject): TagDetailType {
    if (!jsonObject) {
      return undefined;
    }
    const instance = new TagDetailType();
    instance.name = jsonObject.name;
    return instance;
  }
}

