// this class is generated, please do not modify

export class NetworkExtraMemberRelation {
  readonly memberId: number;

  constructor(memberId: number) {
    this.memberId = memberId;
  }

  public static fromJSON(jsonObject): NetworkExtraMemberRelation {
    if (!jsonObject) {
      return undefined;
    }
    return new NetworkExtraMemberRelation(
      jsonObject.memberId
    );
  }
}
