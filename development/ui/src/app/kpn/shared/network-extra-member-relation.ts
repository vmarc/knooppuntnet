// this class is generated, please do not modify

export class NetworkExtraMemberRelation {

  constructor(public memberId?: number) {
  }

  public static fromJSON(jsonObject): NetworkExtraMemberRelation {
    if (!jsonObject) {
      return undefined;
    }
    const instance = new NetworkExtraMemberRelation();
    instance.memberId = jsonObject.memberId;
    return instance;
  }
}

