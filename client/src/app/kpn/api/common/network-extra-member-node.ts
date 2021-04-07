// this class is generated, please do not modify

export class NetworkExtraMemberNode {
  constructor(readonly memberId: number) {}

  static fromJSON(jsonObject: any): NetworkExtraMemberNode {
    if (!jsonObject) {
      return undefined;
    }
    return new NetworkExtraMemberNode(jsonObject.memberId);
  }
}
