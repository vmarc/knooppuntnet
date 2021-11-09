import { StatisticValues } from '@api/common/statistics/statistic-values';
import { Subset } from '@api/custom/subset';
import { StatisticConfiguration } from './statistic-configuration';

export class Stat {
  constructor(
    readonly statisticValues: StatisticValues,
    readonly configuration: StatisticConfiguration
  ) {}

  total(): number {
    if (!this.statisticValues) {
      return undefined;
    }
    return this.statisticValues.values.reduce((sum, statisticValue) => {
      return sum + statisticValue.value;
    }, 0);
  }

  value(subset: Subset): number {
    if (!this.statisticValues) {
      return undefined;
    }
    const subsetStatisticValue = this.statisticValues.values.find(
      (statisticValue) => {
        return (
          statisticValue.country === subset.country &&
          statisticValue.networkType === subset.networkType
        );
      }
    );
    if (!subsetStatisticValue) {
      return undefined;
    }
    return subsetStatisticValue.value;
  }
}
