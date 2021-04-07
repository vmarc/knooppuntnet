// this class is generated, please do not modify

export class RouteRoleDiff {
  constructor(readonly before: string, readonly after: string) {}

  public static fromJSON(jsonObject: any): RouteRoleDiff {
    if (!jsonObject) {
      return undefined;
    }
    return new RouteRoleDiff(jsonObject.before, jsonObject.after);
  }
}
