// this class is generated, please do not modify

export class Tag {

  constructor(public key?: string,
              public value?: string) {
  }

  public static fromJSON(jsonObject): Tag {
    if (!jsonObject) {
      return undefined;
    }
    const instance = new Tag();
    instance.key = jsonObject.key;
    instance.value = jsonObject.value;
    return instance;
  }
}

