// this class is generated, please do not modify

export class NodeIntegrityCheck {
  readonly nodeName: string;
  readonly nodeId: number;
  readonly actual: number;
  readonly expected: number;
  readonly failed: boolean;

  constructor(nodeName: string,
              nodeId: number,
              actual: number,
              expected: number,
              failed: boolean) {
    this.nodeName = nodeName;
    this.nodeId = nodeId;
    this.actual = actual;
    this.expected = expected;
    this.failed = failed;
  }

  public static fromJSON(jsonObject): NodeIntegrityCheck {
    if (!jsonObject) {
      return undefined;
    }
    return new NodeIntegrityCheck(
      jsonObject.nodeName,
      jsonObject.nodeId,
      jsonObject.actual,
      jsonObject.expected,
      jsonObject.failed
    );
  }
}
