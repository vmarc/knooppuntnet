// this class is generated, please do not modify

export class NetworkIntegrityCheck {
  constructor(readonly count: number, readonly failed: number) {}

  public static fromJSON(jsonObject: any): NetworkIntegrityCheck {
    if (!jsonObject) {
      return undefined;
    }
    return new NetworkIntegrityCheck(jsonObject.count, jsonObject.failed);
  }
}
