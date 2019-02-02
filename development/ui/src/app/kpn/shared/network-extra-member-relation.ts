// this class is generated, please do not modify

export class NetworkExtraMemberRelation {

  constructor(readonly memberId: number) {
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
