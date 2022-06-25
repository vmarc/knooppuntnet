import { StatisticValues } from '@api/common/statistics/statistic-values';
import { Subset } from '@api/custom/subset';
import { StatisticConfiguration } from './statistic-configuration';

export class Stat {
  constructor(
    readonly statisticValues: StatisticValues,
    readonly configuration: StatisticConfiguration
  ) {}

  total(): string {
    if (!this.statisticValues) {
      return undefined;
    }
    return this.statisticValues.total;
  }

  value(subset: Subset): string {
    if (!this.statisticValues) {
      return '-';
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
      return '-';
    }
    return subsetStatisticValue.value;
  }
}
