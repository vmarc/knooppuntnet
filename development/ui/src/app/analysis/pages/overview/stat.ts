import {StatisticConfiguration} from "./statistic-configuration";
import {Statistic} from "../../../kpn/shared/statistics/statistic";
import {Subset} from "../../../kpn/shared/subset";

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

  value(subset: Subset) {
    if (this.figures === null) {
      return "-";
    }
    return this.figures[subset.country.domain][subset.networkType.name];
  }

}
