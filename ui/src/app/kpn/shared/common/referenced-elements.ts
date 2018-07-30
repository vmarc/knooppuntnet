// this class is generated, please do not modify

export class ReferencedElements {

  constructor(public nodeIds?: Array<number>,
              public routeIds?: Array<number>) {
  }

  public static fromJSON(jsonObject): ReferencedElements {
    if (!jsonObject) {
      return undefined;
    }
    const instance = new ReferencedElements();
    instance.nodeIds = jsonObject.nodeIds;
    instance.routeIds = jsonObject.routeIds;
    return instance;
  }
}

