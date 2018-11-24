// this class is generated, please do not modify

import {CountryStatistic} from './country-statistic';

export class Statistic {
  readonly total: string;
  readonly nl: CountryStatistic;
  readonly be: CountryStatistic;
  readonly de: CountryStatistic;

  constructor(total: string,
              nl: CountryStatistic,
              be: CountryStatistic,
              de: CountryStatistic) {
    this.total = total;
    this.nl = nl;
    this.be = be;
    this.de = de;
  }

  public static fromJSON(jsonObject): Statistic {
    if (!jsonObject) {
      return undefined;
    }
    return new Statistic(
      jsonObject.total,
      CountryStatistic.fromJSON(jsonObject.nl),
      CountryStatistic.fromJSON(jsonObject.be),
      CountryStatistic.fromJSON(jsonObject.de)
    );
  }
}
