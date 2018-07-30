// this class is generated, please do not modify

export class NetworkExtraMemberNode {

  constructor(public memberId?: number) {
  }

  public static fromJSON(jsonObject): NetworkExtraMemberNode {
    if (!jsonObject) {
      return undefined;
    }
    const instance = new NetworkExtraMemberNode();
    instance.memberId = jsonObject.memberId;
    return instance;
  }
}

