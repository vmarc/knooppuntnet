// this class is generated, please do not modify

export class ChangeSetElementRef {

  constructor(public id?: number,
              public name?: string,
              public happy?: boolean,
              public investigate?: boolean) {
  }

  public static fromJSON(jsonObject): ChangeSetElementRef {
    if (!jsonObject) {
      return undefined;
    }
    const instance = new ChangeSetElementRef();
    instance.id = jsonObject.id;
    instance.name = jsonObject.name;
    instance.happy = jsonObject.happy;
    instance.investigate = jsonObject.investigate;
    return instance;
  }
}

