// this class is generated, please do not modify

export class NetworkIntegrityCheck {

  constructor(public count?: number,
              public failed?: number) {
  }

  public static fromJSON(jsonObject): NetworkIntegrityCheck {
    if (!jsonObject) {
      return undefined;
    }
    const instance = new NetworkIntegrityCheck();
    instance.count = jsonObject.count;
    instance.failed = jsonObject.failed;
    return instance;
  }
}

