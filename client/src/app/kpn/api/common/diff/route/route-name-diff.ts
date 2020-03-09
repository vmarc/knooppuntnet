// this class is generated, please do not modify

export class RouteNameDiff {

  constructor(readonly before: string,
              readonly after: string) {
  }

  public static fromJSON(jsonObject: any): RouteNameDiff {
    if (!jsonObject) {
      return undefined;
    }
    return new RouteNameDiff(
      jsonObject.before,
      jsonObject.after
    );
  }
}
