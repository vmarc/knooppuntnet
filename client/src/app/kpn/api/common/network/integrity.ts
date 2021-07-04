// this class is generated, please do not modify

export class Integrity {
  constructor(
    readonly isOk: boolean,
    readonly hasChecks: boolean,
    readonly count: string,
    readonly okCount: number,
    readonly nokCount: number,
    readonly coverage: string,
    readonly okRate: string,
    readonly nokRate: string
  ) {}

  public static fromJSON(jsonObject: any): Integrity {
    if (!jsonObject) {
      return undefined;
    }
    return new Integrity(
      jsonObject.isOk,
      jsonObject.hasChecks,
      jsonObject.count,
      jsonObject.okCount,
      jsonObject.nokCount,
      jsonObject.coverage,
      jsonObject.okRate,
      jsonObject.nokRate
    );
  }
}
