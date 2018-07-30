// this class is generated, please do not modify

export class RouteRoleDiff {

  constructor(public before?: string,
              public after?: string) {
  }

  public static fromJSON(jsonObject): RouteRoleDiff {
    if (!jsonObject) {
      return undefined;
    }
    const instance = new RouteRoleDiff();
    instance.before = jsonObject.before;
    instance.after = jsonObject.after;
    return instance;
  }
}

