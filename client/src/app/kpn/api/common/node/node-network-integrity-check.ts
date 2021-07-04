// this class is generated, please do not modify

export class NodeNetworkIntegrityCheck {
  constructor(
    readonly failed: boolean,
    readonly expected: number,
    readonly actual: number
  ) {}

  public static fromJSON(jsonObject: any): NodeNetworkIntegrityCheck {
    if (!jsonObject) {
      return undefined;
    }
    return new NodeNetworkIntegrityCheck(
      jsonObject.failed,
      jsonObject.expected,
      jsonObject.actual
    );
  }
}
