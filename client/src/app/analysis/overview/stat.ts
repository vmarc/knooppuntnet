import {Statistic} from "../../kpn/api/common/statistics/statistic";
import {Subset} from "../../kpn/api/custom/subset";
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

  value(subset: Subset) {
    if (this.figures === null) {
      return "-";
    }
    return this.figures[subset.country.domain][subset.networkType.id];
  }

}
