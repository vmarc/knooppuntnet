// this class is generated, please do not modify

export class NetworkSizeEstimate {

  constructor(public nodeCount?: number) {
  }

  public static fromJSON(jsonObject): NetworkSizeEstimate {
    if (!jsonObject) {
      return undefined;
    }
    const instance = new NetworkSizeEstimate();
    instance.nodeCount = jsonObject.nodeCount;
    return instance;
  }
}

