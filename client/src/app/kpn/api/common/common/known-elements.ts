// this class is generated, please do not modify

export class KnownElements {

  constructor(readonly nodeIds: Array<number>,
              readonly routeIds: Array<number>) {
  }

  public static fromJSON(jsonObject: any): KnownElements {
    if (!jsonObject) {
      return undefined;
    }
    return new KnownElements(
      jsonObject.nodeIds,
      jsonObject.routeIds
    );
  }
}
