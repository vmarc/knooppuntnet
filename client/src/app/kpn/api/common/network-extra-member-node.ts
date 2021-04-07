// this class is generated, please do not modify

export class NetworkExtraMemberNode {
  constructor(readonly memberId: number) {}

  public static fromJSON(jsonObject: any): NetworkExtraMemberNode {
    if (!jsonObject) {
      return undefined;
    }
    return new NetworkExtraMemberNode(jsonObject.memberId);
  }
}
