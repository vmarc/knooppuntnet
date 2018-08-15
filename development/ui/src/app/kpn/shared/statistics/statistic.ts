// this class is generated, please do not modify

import {CountryStatistic} from './country-statistic';

export class Statistic {

  constructor(public total?: string,
              public nl?: CountryStatistic,
              public be?: CountryStatistic,
              public de?: CountryStatistic) {
  }

  public static fromJSON(jsonObject): Statistic {
    if (!jsonObject) {
      return undefined;
    }
    const instance = new Statistic();
    instance.total = jsonObject.total;
    instance.nl = CountryStatistic.fromJSON(jsonObject.nl);
    instance.be = CountryStatistic.fromJSON(jsonObject.be);
    instance.de = CountryStatistic.fromJSON(jsonObject.de);
    return instance;
  }
}

