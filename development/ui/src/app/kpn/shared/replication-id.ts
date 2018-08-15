// this class is generated, please do not modify

export class ReplicationId {

  constructor(public level1?: number,
              public level2?: number,
              public level3?: number) {
  }

  public static fromJSON(jsonObject): ReplicationId {
    if (!jsonObject) {
      return undefined;
    }
    const instance = new ReplicationId();
    instance.level1 = jsonObject.level1;
    instance.level2 = jsonObject.level2;
    instance.level3 = jsonObject.level3;
    return instance;
  }
}

