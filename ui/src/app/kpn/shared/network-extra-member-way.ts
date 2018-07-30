// this class is generated, please do not modify

export class NetworkExtraMemberWay {

  constructor(public memberId?: number) {
  }

  public static fromJSON(jsonObject): NetworkExtraMemberWay {
    if (!jsonObject) {
      return undefined;
    }
    const instance = new NetworkExtraMemberWay();
    instance.memberId = jsonObject.memberId;
    return instance;
  }
}

