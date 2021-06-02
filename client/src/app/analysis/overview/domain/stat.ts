import { CountryStatistic } from '@api/common/statistics/country-statistic';
import { Statistic } from '@api/common/statistics/statistic';
import { Subset } from '@api/custom/subset';
import { StatisticConfiguration } from './statistic-configuration';

export class Stat {
  constructor(
    readonly figures: Statistic,
    readonly configuration: StatisticConfiguration
  ) {}

  total() {
    if (this.figures) {
      return this.figures.total;
    }
    return '-';
  }

  value(subset: Subset): string {
    if (this.figures) {
      let countryStatistic: CountryStatistic = null;
      if (subset.country === 'nl') {
        countryStatistic = this.figures.nl;
      } else if (subset.country === 'be') {
        countryStatistic = this.figures.be;
      } else if (subset.country === 'de') {
        countryStatistic = this.figures.de;
      } else if (subset.country === 'fr') {
        countryStatistic = this.figures.fr;
      } else if (subset.country === 'at') {
        countryStatistic = this.figures.at;
      } else if (subset.country === 'es') {
        countryStatistic = this.figures.es;
      } else {
        return '-';
      }

      if (countryStatistic !== null) {
        if (subset.networkType === 'cycling') {
          return countryStatistic.rcn;
        }
        if (subset.networkType === 'hiking') {
          return countryStatistic.rwn;
        }
        if (subset.networkType === 'horse-riding') {
          return countryStatistic.rhn;
        }
        if (subset.networkType === 'motorboat') {
          return countryStatistic.rmn;
        }
        if (subset.networkType === 'canoe') {
          return countryStatistic.rpn;
        }
        if (subset.networkType === 'inline-skating') {
          return countryStatistic.rin;
        }
      }
    }
    return '-';
  }
}
