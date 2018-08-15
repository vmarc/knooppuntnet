export class Tag {

  constructor(public key?: string,
              public value?: string) {
  }

  public static fromJSON(jsonObject): Tag {
    if (!jsonObject) {
      return undefined;
    }
    const instance = new Tag();
    instance.key = jsonObject[0];
    instance.value = jsonObject[1];
    return instance;
  }
}

