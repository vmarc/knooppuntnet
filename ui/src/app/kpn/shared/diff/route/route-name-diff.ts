// this class is generated, please do not modify

export class RouteNameDiff {

  constructor(public before?: string,
              public after?: string) {
  }

  public static fromJSON(jsonObject): RouteNameDiff {
    if (!jsonObject) {
      return undefined;
    }
    const instance = new RouteNameDiff();
    instance.before = jsonObject.before;
    instance.after = jsonObject.after;
    return instance;
  }
}

