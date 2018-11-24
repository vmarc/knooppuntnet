// this class is generated, please do not modify

export class CountryStatistic {
  readonly rwn: string;
  readonly rcn: string;
  readonly rhn: string;
  readonly rmn: string;
  readonly rpn: string;
  readonly rin: string;

  constructor(rwn: string,
              rcn: string,
              rhn: string,
              rmn: string,
              rpn: string,
              rin: string) {
    this.rwn = rwn;
    this.rcn = rcn;
    this.rhn = rhn;
    this.rmn = rmn;
    this.rpn = rpn;
    this.rin = rin;
  }

  public static fromJSON(jsonObject): CountryStatistic {
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
