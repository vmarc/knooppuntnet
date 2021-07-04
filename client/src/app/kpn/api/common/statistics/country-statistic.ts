// this class is generated, please do not modify

export class CountryStatistic {
  constructor(
    readonly rwn: string,
    readonly rcn: string,
    readonly rhn: string,
    readonly rmn: string,
    readonly rpn: string,
    readonly rin: string
  ) {}

  public static fromJSON(jsonObject: any): CountryStatistic {
    if (!jsonObject) {
      return undefined;
    }
    return new CountryStatistic(
      jsonObject.rwn,
      jsonObject.rcn,
      jsonObject.rhn,
      jsonObject.rmn,
      jsonObject.rpn,
      jsonObject.rin
    );
  }
}
