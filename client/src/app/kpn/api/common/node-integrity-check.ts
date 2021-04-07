// this class is generated, please do not modify

export class NodeIntegrityCheck {
  constructor(
    readonly nodeName: string,
    readonly nodeId: number,
    readonly actual: number,
    readonly expected: number,
    readonly failed: boolean
  ) {}

  public static fromJSON(jsonObject: any): NodeIntegrityCheck {
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
