// this class is generated, please do not modify

export class Ref {

  constructor(public id?: number,
              public name?: string) {
  }

  public static fromJSON(jsonObject): Ref {
    if (!jsonObject) {
      return undefined;
    }
    const instance = new Ref();
    instance.id = jsonObject.id;
    instance.name = jsonObject.name;
    return instance;
  }
}

