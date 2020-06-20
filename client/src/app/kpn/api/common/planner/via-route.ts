// this class is generated, please do not modify

export class ViaRoute {

  constructor(readonly routeId: number,
              readonly pathId: number) {
  }

  public static fromJSON(jsonObject: any): ViaRoute {
    if (!jsonObject) {
      return undefined;
    }
    return new ViaRoute(
      jsonObject.routeId,
      jsonObject.pathId
    );
  }
}
