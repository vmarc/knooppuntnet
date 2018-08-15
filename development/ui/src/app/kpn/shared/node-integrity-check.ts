// this class is generated, please do not modify

export class NodeIntegrityCheck {

  constructor(public nodeName?: string,
              public nodeId?: number,
              public actual?: number,
              public expected?: number,
              public failed?: boolean) {
  }

  public static fromJSON(jsonObject): NodeIntegrityCheck {
    if (!jsonObject) {
      return undefined;
    }
    const instance = new NodeIntegrityCheck();
    instance.nodeName = jsonObject.nodeName;
    instance.nodeId = jsonObject.nodeId;
    instance.actual = jsonObject.actual;
    instance.expected = jsonObject.expected;
    instance.failed = jsonObject.failed;
    return instance;
  }
}

