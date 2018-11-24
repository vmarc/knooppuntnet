// this class is generated, please do not modify

export class RouteRoleDiff {
  readonly before: string;
  readonly after: string;

  constructor(before: string,
              after: string) {
    this.before = before;
    this.after = after;
  }

  public static fromJSON(jsonObject): RouteRoleDiff {
    if (!jsonObject) {
      return undefined;
    }
    return new RouteRoleDiff(
      jsonObject.before,
      jsonObject.after
    );
  }
}
