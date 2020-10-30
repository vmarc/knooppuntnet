import {CountryStatistic} from "../../../kpn/api/common/statistics/country-statistic";
import {Statistic} from "../../../kpn/api/common/statistics/statistic";
import {NetworkType} from "../../../kpn/api/custom/network-type";
import {Subset} from "../../../kpn/api/custom/subset";
import {StatisticConfiguration} from "./statistic-configuration";

export class Stat {

  constructor(readonly figures: Statistic,
              readonly configuration: StatisticConfiguration) {
  }

  total() {
    if (this.figures === null) {
      return "-";
    }
    return this.figures.total;
  }

  value(subset: Subset): string {
    if (this.figures === null) {
      return "-";
    }

    let countryStatistic: CountryStatistic = null;
    if (subset.country.domain === "nl") {
      countryStatistic = this.figures.nl;
    } else if (subset.country.domain === "be") {
      countryStatistic = this.figures.be;
    } else if (subset.country.domain === "de") {
      countryStatistic = this.figures.de;
    } else if (subset.country.domain === "fr") {
      countryStatistic = this.figures.fr;
    } else if (subset.country.domain === "at") {
      countryStatistic = this.figures.at;
    } else if (subset.country.domain === "es") {
      countryStatistic = this.figures.es;
    } else {
      return "-";
    }

    if (countryStatistic !== null) {
      if (subset.networkType === NetworkType.cycling) {
        return countryStatistic.rcn;
      }
      if (subset.networkType === NetworkType.hiking) {
        return countryStatistic.rwn;
      }
      if (subset.networkType === NetworkType.horseRiding) {
        return countryStatistic.rhn;
      }
      if (subset.networkType === NetworkType.motorboat) {
        return countryStatistic.rmn;
      }
      if (subset.networkType === NetworkType.canoe) {
        return countryStatistic.rpn;
      }
      if (subset.networkType === NetworkType.inlineSkating) {
        return countryStatistic.rin;
      }
    }
    return "-";
  }

}
