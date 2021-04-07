// this class is generated, please do not modify

export class Check {
  constructor(
    readonly nodeId: number,
    readonly nodeName: string,
    readonly actual: number,
    readonly expected: number
  ) {}

  static fromJSON(jsonObject: any): Check {
    if (!jsonObject) {
      return undefined;
    }
    return new Check(
      jsonObject.nodeId,
      jsonObject.nodeName,
      jsonObject.actual,
      jsonObject.expected
    );
  }
}
