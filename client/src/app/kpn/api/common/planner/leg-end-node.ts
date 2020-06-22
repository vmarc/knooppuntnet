// this class is generated, please do not modify

export class LegEndNode {

  constructor(readonly nodeId: number) {
  }

  public static fromJSON(jsonObject: any): LegEndNode {
    if (!jsonObject) {
      return undefined;
    }
    return new LegEndNode(
      jsonObject.nodeId
    );
  }
}
