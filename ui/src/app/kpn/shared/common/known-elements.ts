// this class is generated, please do not modify

export class KnownElements {

  constructor(public nodeIds?: Array<number>,
              public routeIds?: Array<number>) {
  }

  public static fromJSON(jsonObject): KnownElements {
    if (!jsonObject) {
      return undefined;
    }
    const instance = new KnownElements();
    instance.nodeIds = jsonObject.nodeIds;
    instance.routeIds = jsonObject.routeIds;
    return instance;
  }
}

