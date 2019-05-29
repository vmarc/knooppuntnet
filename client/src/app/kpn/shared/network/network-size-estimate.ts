// this class is generated, please do not modify

export class NetworkSizeEstimate {

  constructor(readonly nodeCount: number) {
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
