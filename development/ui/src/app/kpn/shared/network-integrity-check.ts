// this class is generated, please do not modify

export class NetworkIntegrityCheck {
  readonly count: number;
  readonly failed: number;

  constructor(count: number,
              failed: number) {
    this.count = count;
    this.failed = failed;
  }

  public static fromJSON(jsonObject): NetworkIntegrityCheck {
    if (!jsonObject) {
      return undefined;
    }
    return new NetworkIntegrityCheck(
      jsonObject.count,
      jsonObject.failed
    );
  }
}
