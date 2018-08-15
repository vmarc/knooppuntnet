// this class is generated, please do not modify

export class CountryStatistic {

  constructor(public rwn?: string,
              public rcn?: string) {
  }

  public static fromJSON(jsonObject): CountryStatistic {
    if (!jsonObject) {
      return undefined;
    }
    const instance = new CountryStatistic();
    instance.rwn = jsonObject.rwn;
    instance.rcn = jsonObject.rcn;
    return instance;
  }
}

