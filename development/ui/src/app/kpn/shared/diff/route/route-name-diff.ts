// this class is generated, please do not modify

export class RouteNameDiff {
  readonly before: string;
  readonly after: string;

  constructor(before: string,
              after: string) {
    this.before = before;
    this.after = after;
  }

  public static fromJSON(jsonObject): RouteNameDiff {
    if (!jsonObject) {
      return undefined;
    }
    return new RouteNameDiff(
      jsonObject.before,
      jsonObject.after
    );
  }
}
