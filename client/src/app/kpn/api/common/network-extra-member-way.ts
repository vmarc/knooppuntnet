// this class is generated, please do not modify

export class NetworkExtraMemberWay {
  constructor(readonly memberId: number) {}

  public static fromJSON(jsonObject: any): NetworkExtraMemberWay {
    if (!jsonObject) {
      return undefined;
    }
    return new NetworkExtraMemberWay(jsonObject.memberId);
  }
}
