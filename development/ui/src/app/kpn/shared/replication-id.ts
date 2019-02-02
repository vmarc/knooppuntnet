// this class is generated, please do not modify

export class ReplicationId {

  constructor(readonly level1: number,
              readonly level2: number,
              readonly level3: number) {
  }

  public static fromJSON(jsonObject): ReplicationId {
    if (!jsonObject) {
      return undefined;
    }
    return new ReplicationId(
      jsonObject.level1,
      jsonObject.level2,
      jsonObject.level3
    );
  }
}
