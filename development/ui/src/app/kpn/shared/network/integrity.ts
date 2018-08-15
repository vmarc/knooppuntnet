// this class is generated, please do not modify

export class Integrity {

  constructor(public isOk?: boolean,
              public hasChecks?: boolean,
              public count?: string,
              public okCount?: number,
              public nokCount?: number,
              public coverage?: string,
              public okRate?: string,
              public nokRate?: string) {
  }

  public static fromJSON(jsonObject): Integrity {
    if (!jsonObject) {
      return undefined;
    }
    const instance = new Integrity();
    instance.isOk = jsonObject.isOk;
    instance.hasChecks = jsonObject.hasChecks;
    instance.count = jsonObject.count;
    instance.okCount = jsonObject.okCount;
    instance.nokCount = jsonObject.nokCount;
    instance.coverage = jsonObject.coverage;
    instance.okRate = jsonObject.okRate;
    instance.nokRate = jsonObject.nokRate;
    return instance;
  }
}

