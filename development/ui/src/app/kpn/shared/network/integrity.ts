// this class is generated, please do not modify

export class Integrity {
  readonly isOk: boolean;
  readonly hasChecks: boolean;
  readonly count: string;
  readonly okCount: number;
  readonly nokCount: number;
  readonly coverage: string;
  readonly okRate: string;
  readonly nokRate: string;

  constructor(isOk: boolean,
              hasChecks: boolean,
              count: string,
              okCount: number,
              nokCount: number,
              coverage: string,
              okRate: string,
              nokRate: string) {
    this.isOk = isOk;
    this.hasChecks = hasChecks;
    this.count = count;
    this.okCount = okCount;
    this.nokCount = nokCount;
    this.coverage = coverage;
    this.okRate = okRate;
    this.nokRate = nokRate;
  }

  public static fromJSON(jsonObject): Integrity {
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
