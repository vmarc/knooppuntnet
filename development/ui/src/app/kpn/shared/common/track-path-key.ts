// this class is generated, please do not modify

export class TrackPathKey {

  constructor(readonly routeId: number,
              readonly pathType: string,
              readonly pathIndex: number) {
  }

  public static fromJSON(jsonObject): TrackPathKey {
    if (!jsonObject) {
      return undefined;
    }
    return new TrackPathKey(
      jsonObject.routeId,
      jsonObject.pathType,
      jsonObject.pathIndex
    );
  }
}
