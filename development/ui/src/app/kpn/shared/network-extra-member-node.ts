// this class is generated, please do not modify

export class NetworkExtraMemberNode {
  readonly memberId: number;

  constructor(memberId: number) {
    this.memberId = memberId;
  }

  public static fromJSON(jsonObject): NetworkExtraMemberNode {
    if (!jsonObject) {
      return undefined;
    }
    return new NetworkExtraMemberNode(
      jsonObject.memberId
    );
  }
}
