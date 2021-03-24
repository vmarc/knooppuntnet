// this class is generated, please do not modify

export class ReplicationId {

  constructor(readonly level1: number,
              readonly level2: number,
              readonly level3: number) {
  }

  static fromJSON(jsonObject: any): ReplicationId {
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
