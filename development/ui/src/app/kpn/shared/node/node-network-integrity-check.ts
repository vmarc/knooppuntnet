// this class is generated, please do not modify

export class NodeNetworkIntegrityCheck {
  readonly failed: boolean;
  readonly expected: number;
  readonly actual: number;

  constructor(failed: boolean,
              expected: number,
              actual: number) {
    this.failed = failed;
    this.expected = expected;
    this.actual = actual;
  }

  public static fromJSON(jsonObject): NodeNetworkIntegrityCheck {
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
