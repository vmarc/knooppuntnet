// this class is generated, please do not modify

export class LegEndRoute {

  constructor(readonly routeId: number,
              readonly pathId: number) {
  }

  public static fromJSON(jsonObject: any): LegEndRoute {
    if (!jsonObject) {
      return undefined;
    }
    return new LegEndRoute(
      jsonObject.routeId,
      jsonObject.pathId
    );
  }
}
