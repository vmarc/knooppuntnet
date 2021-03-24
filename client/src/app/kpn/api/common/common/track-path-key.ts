// this class is generated, please do not modify

export class TrackPathKey {

  constructor(readonly routeId: number,
              readonly pathId: number) {
  }

  static fromJSON(jsonObject: any): TrackPathKey {
    if (!jsonObject) {
      return undefined;
    }
    return new TrackPathKey(
      jsonObject.routeId,
      jsonObject.pathId
    );
  }
}
