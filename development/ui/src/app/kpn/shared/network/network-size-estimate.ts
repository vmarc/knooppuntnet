// this class is generated, please do not modify

export class NetworkSizeEstimate {
  readonly nodeCount: number;

  constructor(nodeCount: number) {
    this.nodeCount = nodeCount;
  }

  public static fromJSON(jsonObject): NetworkSizeEstimate {
    if (!jsonObject) {
      return undefined;
    }
    return new NetworkSizeEstimate(
      jsonObject.nodeCount
    );
  }
}
