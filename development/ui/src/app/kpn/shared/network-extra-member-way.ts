// this class is generated, please do not modify

export class NetworkExtraMemberWay {
  readonly memberId: number;

  constructor(memberId: number) {
    this.memberId = memberId;
  }

  public static fromJSON(jsonObject): NetworkExtraMemberWay {
    if (!jsonObject) {
      return undefined;
    }
    return new NetworkExtraMemberWay(
      jsonObject.memberId
    );
  }
}
