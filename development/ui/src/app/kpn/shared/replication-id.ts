// this class is generated, please do not modify

export class ReplicationId {
  readonly level1: number;
  readonly level2: number;
  readonly level3: number;

  constructor(level1: number,
              level2: number,
              level3: number) {
    this.level1 = level1;
    this.level2 = level2;
    this.level3 = level3;
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
